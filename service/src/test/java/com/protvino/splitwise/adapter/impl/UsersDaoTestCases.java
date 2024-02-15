package com.protvino.splitwise.adapter.impl;

import com.protvino.splitwise.adapter.UserDao;
import com.protvino.splitwise.domain.request.EditUserRequest;
import com.protvino.splitwise.domain.value.Person;
import com.protvino.splitwise.domain.value.User;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("DAO")
abstract class UsersDaoTestCases {

    abstract UserDao getDao();

    UserDao userDao = getDao();

    @Test
    void create__when_does_not_exist() {
        // Arrange
        String userName = "userName";
        String password = "password";

        // Act
        Long actualId = userDao.create(new EditUserRequest(userName, password));
        User actualResult = userDao.findByUserName(userName);

        // Assert
        User expectedResult = new User(actualId, userName, password);

        assertThat(actualResult)
                .isEqualTo(expectedResult);
    }
    @Test
    void find_byUserName__when_exists() {
        // Arrange
        String userName = "userName";
        String password = "password";
        Long id = userDao.create(new EditUserRequest(userName, password));

        // Act
        User actualResult = userDao.findByUserName(userName);

        // Assert
        User expectedResult = new User(id, userName, password);

        assertThat(actualResult)
                .isEqualTo(expectedResult);
    }

    @Test
    void find_byUserName__when_does_not_exist() {
        // Arrange
        String userName = "userName";
        String password = "password";
        Long id = userDao.create(new EditUserRequest(userName, password));

        // Act
        User actualResult = userDao.findByUserName("userNotExist");

        // Assert
        assertThat(actualResult)
                .isNull();
    }
}
