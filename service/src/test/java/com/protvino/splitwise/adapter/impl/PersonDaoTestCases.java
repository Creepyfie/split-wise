package com.protvino.splitwise.adapter.impl;

import com.protvino.splitwise.adapter.PersonDao;
import com.protvino.splitwise.domain.request.EditPersonRequest;
import com.protvino.splitwise.domain.value.Person;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("DAO")
abstract class PersonDaoTestCases {

    abstract PersonDao getDao();

    PersonDao personDao = getDao();

    @Test
    void create__when_does_not_exist() {
        // Arrange
        String firstName = "firstName";
        String lastName = "lastName";

        // Act
        Long actualId = personDao.create(new EditPersonRequest(firstName, lastName));
        Person actualResult = personDao.findById(actualId);

        // Assert
        Person expectedResult = new Person(actualId, firstName, lastName);

        assertThat(actualResult)
            .isEqualTo(expectedResult);
    }

    @Test
    void update__when_exists() {
        // Arrange
        String firstName = "firstName";
        String lastName = "lastName";
        Long actualId = personDao.create(new EditPersonRequest(firstName, lastName));

        // Act
        String updateFirstName = "updatedFirstName";
        String updateLastName = "updateLastName";
        personDao.update(actualId, new EditPersonRequest(updateFirstName, updateLastName));

        Person actualResult = personDao.findById(actualId);

        // Assert
        Person expectedResult = new Person(actualId, updateFirstName, updateLastName);

        assertThat(actualResult)
            .isEqualTo(expectedResult);
    }

    @Test
    void update__when_does_not_exist() {
        // Arrange
        // Act
        String updateFirstName = "updatedFirstName";
        String updateLastName = "updateLastName";
        personDao.update(123L, new EditPersonRequest(updateFirstName, updateLastName));

        Person actualResult = personDao.findById(123L);

        // Assert
        assertThat(actualResult)
            .isNull();
    }

    @Test
    void find_byId__when_exists() {
        // Arrange
        String firstName = "firstName";
        String lastName = "lastName";
        Long actualId = personDao.create(new EditPersonRequest(firstName, lastName));

        // Act
        Person actualResult = personDao.findById(actualId);

        // Assert
        Person expectedResult = new Person(actualId, firstName, lastName);

        assertThat(actualResult)
            .isEqualTo(expectedResult);
    }

    @Test
    void find_byId__when_does_not_exist() {
        // Arrange
        String firstName = "firstName";
        String lastName = "lastName";
        personDao.create(new EditPersonRequest(firstName, lastName));

        // Act
        Person actualResult = personDao.findById(123L);

        // Assert
        assertThat(actualResult)
            .isNull();
    }
}
