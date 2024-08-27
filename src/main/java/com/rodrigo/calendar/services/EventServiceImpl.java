package com.rodrigo.calendar.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rodrigo.calendar.models.Event;
import com.rodrigo.calendar.models.dto.UserDto;
import com.rodrigo.calendar.repositories.EventRepository;

@Service
public class EventServiceImpl implements EventService{

    @Autowired
    EventRepository repository;
    @Autowired
    UserService userService;

    @Override
    @Transactional(readOnly = true)
    public List<Event> getEvents (){
        return repository.findAll();
    }

    @Override
    @Transactional
    public ResponseEntity<?> insertEvent(Event event) {
        UserDto userDto = userService.getCurrentUserId();
        System.out.println(userDto);
        if (userDto != null) {
            event.setUserDto(userDto);;
            Event eventSaved = repository.insert(event);
            Map<String, Object> bodyResponse = new HashMap<>();
            bodyResponse.put("ok", true);
            bodyResponse.put("evento", eventSaved );
            return ResponseEntity.status(HttpStatus.CREATED).body(bodyResponse);
        }
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Error al guardar usuario");
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateEvent(String idEvent, Event event){
        Map<String, Object> responseBody = new HashMap<>();
        Optional<Event> eventOptional = repository.findById(idEvent);

        if (!eventOptional.isPresent()) {
            responseBody.put("ok", false);
            responseBody.put("message", "Evento inexistente");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        }

        Event eventDb = eventOptional.get();
        UserDto userCurrent = userService.getCurrentUserId();
        System.out.println("El id del evento es: " + eventDb.getUserDto().getId());
        System.out.println("El id del usuario activo es: " + userCurrent.getId());
        if (!eventDb.getUserDto().getId().equals(userCurrent.getId())) {
            responseBody.put("ok", false);
            responseBody.put("message", "No tiene permiso de modificar este evento");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
        }

        event.setId(idEvent);
        event.setUserDto(userCurrent);
        repository.save(event);
        responseBody.put("ok", true);
        responseBody.put("event", event);

        return ResponseEntity.ok().body(responseBody);
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteEvent(String idEvent){
        Map<String, Object> responseBody = new HashMap<>();
        Optional<Event> eventOptional = repository.findById(idEvent);

        if (!eventOptional.isPresent()) {
            responseBody.put("ok", false);
            responseBody.put("message", "Evento inexistente");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        }

        Event eventDb = eventOptional.get();
        UserDto userCurrent = userService.getCurrentUserId();
        // System.out.println("El id del evento es: " + eventDb.getUserDto().getId());
        // System.out.println("El id del usuario activo es: " + userCurrent.getId());
        if (!eventDb.getUserDto().getId().equals(userCurrent.getId())) {
            responseBody.put("ok", false);
            responseBody.put("message", "No tiene permiso de eliminar este evento");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
        }

        repository.deleteById(idEvent);
        responseBody.put("ok", true);

        return ResponseEntity.ok().body(responseBody);
    }


}
