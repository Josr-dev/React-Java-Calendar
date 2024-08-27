package com.rodrigo.calendar.models.dto.mapper;

import com.rodrigo.calendar.models.User;
import com.rodrigo.calendar.models.dto.UserDto;

public class DtoMapperUser {

    private static DtoMapperUser mapper;

    private User user;

    private DtoMapperUser(){

    }

    public static DtoMapperUser getInstance(){
        mapper = new DtoMapperUser();
        return mapper;
    }

    public DtoMapperUser setUser(User user) {
        this.user = user;
        return mapper;
    }

    public UserDto build(){
        if (user == null) {
            throw new RuntimeException("Debe pasar el entity user");
        }
        return new UserDto(this.user.getId().toString(), user.getUsername(), user.getEmail());
    }
}
