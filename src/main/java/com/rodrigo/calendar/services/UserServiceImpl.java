package com.rodrigo.calendar.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rodrigo.calendar.models.User;
import com.rodrigo.calendar.models.dto.UserDto;
import com.rodrigo.calendar.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> insert(User user) {

        Optional<User> userOptional = repository.findByUsername(user.getUsername());
        if ( userOptional.isPresent() ) {
            return ResponseEntity.badRequest().body("El usuario ya esta en uso");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User userSaved = repository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userSaved);
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

}
