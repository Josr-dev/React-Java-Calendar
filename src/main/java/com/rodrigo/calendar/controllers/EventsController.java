package com.rodrigo.calendar.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rodrigo.calendar.models.Event;
import com.rodrigo.calendar.services.EventService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/events")
public class EventsController {

    @Autowired
    EventService service;

    @GetMapping
    public ResponseEntity<?> traerEventos(){
        Map<String, Object> bodyResponse = new HashMap<>();
        List<Event> events = service.getEvents();
        bodyResponse.put("ok", true);
        bodyResponse.put("eventos", events);
        return ResponseEntity.ok().body(bodyResponse) ;
    }

    @PostMapping
    public ResponseEntity<?> crearEvento(@Valid @RequestBody Event event, @RequestHeader Map<String, Object> object){
        // Map<String, Object> bodyResponse = new HashMap<>();
        // bodyResponse.put("ok", true);
        // bodyResponse.put("message", "Crear Eventos");
        return  service.insertEvent(event);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarEvento(@PathVariable String id,@Valid @RequestBody Event event ){
        return service.updateEvent(id, event);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEvento(@PathVariable String id){
        return service.deleteEvent(id);
    }
}
