package com.rodrigo.calendar.services;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rodrigo.calendar.auth.TokenJwtConfig;
import com.rodrigo.calendar.models.User;
import com.rodrigo.calendar.models.dto.UserDto;
import com.rodrigo.calendar.repositories.UserRepository;

import io.jsonwebtoken.Jwts;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> insert(User user) {
        Map<String, Object> bodyResponse = new HashMap<>();
        
        Optional<User> userOptional = repository.findByUsername(user.getUsername());
        if ( userOptional.isPresent() ) {
            bodyResponse.put("ok", false);
            bodyResponse.put("message","El usuario ya esta en uso");
            return ResponseEntity.badRequest().body(bodyResponse);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User userSaved = repository.save(user);

        bodyResponse.put("ok", true);
        bodyResponse.put("username", userSaved.getUsername());
        bodyResponse.put("id", userSaved.getId().toString());
        bodyResponse.put("email", userSaved.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(bodyResponse);
    }

    @Override
    public List<User> list() {
        return repository.findAll();
    }

    public UserDto getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername();
                Optional<User> userOptional = repository.findByUsername(username);
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    return new UserDto(user.getId().toString(), user.getUsername(), user.getEmail());
                }
            } else if (principal instanceof String) {
                String username = principal.toString();
                Optional<User> userOptional = repository.findByUsername(username);
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    return new UserDto(user.getId().toString(), user.getUsername(), user.getEmail());
                }
            }
        }
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        Optional<User> userOptional = repository.findByUsername(username);
        if (!userOptional.isPresent()) {
            return null;
        }
        return userOptional.orElseThrow();
    }

    public ResponseEntity<?> getNewToken(){
        Map<String, Object> response = new HashMap<>();
        UserDto userDto = getCurrentUserId();

        String token = 
            Jwts
            .builder()
            .subject(userDto.getUsername())
            .signWith(TokenJwtConfig.SECRET_KEY)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 3600000))
            .compact();
        

        response.put("ok", true);
        response.put("token", token);
        response.put("username", userDto.getUsername());
        response.put("id", userDto.getId().toString());
        return ResponseEntity.ok().body(response);
    }

    

}
