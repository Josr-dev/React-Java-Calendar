package com.rodrigo.calendar.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rodrigo.calendar.repositories.UserRepository;

@Service
public class JpaUserDetailsService implements UserDetailsService{

    @Autowired
    private UserRepository repository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<com.rodrigo.calendar.models.User> userOptional = repository.findByUsername(username);

        if (!userOptional.isPresent()){
            throw new UsernameNotFoundException(String.format("Username %s no existe en el sistema", username));
        }

        com.rodrigo.calendar.models.User user = userOptional.orElseThrow();

        List<GrantedAuthority> authorities = new ArrayList<>(); 
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new User(username,
            user.getPassword(),
            true,
            true,
            true,
            true, 
            authorities);
    }

}
