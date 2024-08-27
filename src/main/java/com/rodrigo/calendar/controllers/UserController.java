package com.rodrigo.calendar.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rodrigo.calendar.models.User;
import com.rodrigo.calendar.services.UserService;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    UserService service;

    // @PostMapping("/new")
    // public Map<String, Object> crearUsuario(@Valid @RequestBody Map<String, Object> param) {
    //     Map<String, Object> response = new HashMap<>();

    //     response.put("ok", true);
    //     response.put("message", "Registro");
    //     response.put("name", param.get("username"));
    //     response.put("email", param.get("email"));
    //     response.put("password", param.get("password"));

    //     return response;
    // }

    @PostMapping
    public Map<String, Object> loginUsuario(@Valid @RequestBody Map<String, Object> param) {
        Map<String, Object> response = new HashMap<>();

        response.put("ok", true);
        response.put("message", "Login");
        response.put("email", param.get("email"));
        response.put("password", param.get("password|"));

        return response;
    }

    @PostMapping("/new")
    public ResponseEntity<?> crearUsuario(@Valid @RequestBody User user) {
        return service.insert(user);
    }

    @GetMapping("/users")
    public List<User> traerUsuarios() {
        System.out.println("Usuarios traidos");
        return service.list();
    }
}
