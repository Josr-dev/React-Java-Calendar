package com.rodrigo.calendar.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.rodrigo.calendar.models.Event;

public interface EventService {
    ResponseEntity<?> insertEvent(Event event);

    List<Event> getEvents();

    ResponseEntity<?> updateEvent(String id, Event event);

    ResponseEntity<?> deleteEvent(String id);
}
