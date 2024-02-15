package com.protvino.splitwise.service.impl;

import com.protvino.splitwise.adapter.impl.SqlUserDao;
import com.protvino.splitwise.domain.request.EditUserRequest;
import com.protvino.splitwise.port.dto.UserDto;
import com.protvino.splitwise.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final SqlUserDao userDao;

    @Override
    public void saveUser(UserDto userDto) {

        EditUserRequest request= new EditUserRequest(userDto.getUserName(), passwordEncoder.encode(userDto.getPassword()));
        userDao.create(request);
    }

    @Override
    public List<UserDto> findAllUsers() {
        return null;
    }
}
