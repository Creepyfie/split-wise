package com.protvino.splitwise.service;

import com.protvino.splitwise.adapter.UserDao;
import com.protvino.splitwise.exceptions.UserAlreadyExistExeption;
import com.protvino.splitwise.port.dto.UserDto;
import com.protvino.splitwise.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AcceptRegistrationInAppCase {

    private final UserDao userDao;
    private final UserServiceImpl userService;

    public void acceptRegistration(String userName, String password) {

        boolean userExists = userDao.checkIfExists(userName);
        if (userExists) {
            userService.saveUser(new UserDto(0l, userName,password));
        } else {
            throw new UserAlreadyExistExeption();
        }
    }
}
