package com.rodrigo.calendar.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.rodrigo.calendar.models.User;

public interface UserRepository extends MongoRepository<User, Object>{

    Optional<User> findByUsername(String username);
}
