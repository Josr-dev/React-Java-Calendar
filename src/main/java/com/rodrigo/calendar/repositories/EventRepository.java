package com.rodrigo.calendar.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.rodrigo.calendar.models.Event;

public interface EventRepository extends MongoRepository<Event, Object> {

}
