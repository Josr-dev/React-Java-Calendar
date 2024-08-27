package com.rodrigo.calendar.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.rodrigo.calendar.models.User;
import com.rodrigo.calendar.models.dto.UserDto;

public interface UserService {

    ResponseEntity<?> insert(User user);

    List<User> list();

    public UserDto getCurrentUserId();
}
