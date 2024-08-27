package com.rodrigo.calendar.models;

import java.time.LocalDateTime;

// import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.rodrigo.calendar.models.dto.UserDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Document(collection = "events")
public class Event {

    @Id
    private String id;

    @NotBlank(message = "El titulo es obligatorio")
    private String title;

    @NotBlank(message = "Las notas son obligatorias")
    private String notes;

    @NotNull
    private LocalDateTime start;

    @NotNull
    private LocalDateTime end;

    private UserDto userDto;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    

}
