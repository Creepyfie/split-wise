package com.protvino.splitwise.service;

import com.protvino.splitwise._inmem.InMemoryUserDao;
import com.protvino.splitwise.domain.request.EditUserRequest;
import com.protvino.splitwise.domain.value.User;
import com.protvino.splitwise.exceptions.UserAlreadyExistsExeption;
import com.protvino.splitwise.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@Tag("UNIT")
public class AcceptRegistrationInAppCaseTest {

    InMemoryUserDao userDao= new InMemoryUserDao();

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    UserServiceImpl userService = new UserServiceImpl(passwordEncoder, userDao);

    RegistrationService aCase = new RegistrationService(userDao, userService);

    @BeforeEach
    void setup() {
        userDao.clear();
    }

    @Test
    void accept_registrationErr__whenUser_has_already_existed() {
        // Arrange
        String userName = "test_User";
        String password = "test_password";
        userDao.create(new EditUserRequest(userName, passwordEncoder.encode(password)));

        // Act
        Throwable e = catchThrowable(() -> aCase.register(userName, password));

        // Assert
        assertThat(e).isInstanceOf(UserAlreadyExistsExeption.class);
    }

    @Test
    void accept_registration_whenUser_has_no_already_existed() {
        // Arrange
        String userName = "test_User";
        String password = "test_password";

        // Act
        aCase.register(userName,password);

        // Assert
        User expectedUser = userDao.findByUserName(userName);


        if (passwordEncoder.matches(password,expectedUser.getPassword()))
        assertThat(expectedUser.getUserName())
            .isEqualTo(userName);
    }
}
