package com.protvino.splitwise.service;

import com.protvino.splitwise.port.dto.UserDto;

import java.util.List;

public interface UserService {

    void saveUser(UserDto userDto);

    List<UserDto> findAllUsers();
}
