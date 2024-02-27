package com.protvino.splitwise.adapter.impl;

import com.protvino.splitwise.adapter.ParticipantDao;
import com.protvino.splitwise.adapter.PersonDao;
import com.protvino.splitwise.domain.request.EditParticipantRequest;
import com.protvino.splitwise.domain.request.EditPersonRequest;
import com.protvino.splitwise.domain.value.Participant;
import com.protvino.splitwise.domain.value.Person;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class ParticipantDaoTestCases {

    abstract ParticipantDao getDao();

    ParticipantDao participantDao = getDao();

    @Test
    void create__when_does_not_exist() {
        // Arrange
        Long personId = ThreadLocalRandom.current().nextLong();
        Long groupId = ThreadLocalRandom.current().nextLong();

        // Act
        Long actualId = participantDao.create(new EditParticipantRequest(personId, groupId));

        Participant actualResult = participantDao.findById(actualId);

        // Assert
        Participant expectedResult = new Participant(actualId, personId, groupId);

        assertThat(actualResult)
                .isEqualTo(expectedResult);
    }

    @Test
    void update__when_exists() {

        // Arrange
        Long personId = ThreadLocalRandom.current().nextLong();
        Long groupId = ThreadLocalRandom.current().nextLong();
        Long actualId = participantDao.create(new EditParticipantRequest(personId, groupId));

        // Act
        Long updatedPersonId = ThreadLocalRandom.current().nextLong();
        Long updatedGroupId = ThreadLocalRandom.current().nextLong();
        participantDao.update(actualId, new EditParticipantRequest(updatedPersonId, updatedGroupId));

        Participant actualResult = participantDao.findById(actualId);

        // Assert
        Participant expectedResult = new Participant(actualId, updatedPersonId, updatedGroupId);

        assertThat(actualResult)
                .isEqualTo(expectedResult);
    }

    @Test
    void update__when_does_not_exist() {
        // Arrange
        // Act
        Long updatedPersonId = ThreadLocalRandom.current().nextLong();
        Long updatedGroupId = ThreadLocalRandom.current().nextLong();
        participantDao.update(123L, new EditParticipantRequest(updatedPersonId, updatedGroupId));

        Participant actualResult = participantDao.findById(123L);

        // Assert
        assertThat(actualResult)
                .isNull();
    }

    @Test
    void find_byId__when_exists() {
        // Arrange
        Long personId = ThreadLocalRandom.current().nextLong();
        Long groupId = ThreadLocalRandom.current().nextLong();
        Long actualId = participantDao.create(new EditParticipantRequest(personId, groupId));

        // Act
        Participant actualResult = participantDao.findById(actualId);

        // Assert
        Participant expectedResult = new Participant(actualId, personId, groupId);

        assertThat(actualResult)
                .isEqualTo(expectedResult);
    }

    @Test
    void find_byId__when_does_not_exist() {
        // Arrange
        Long personId = ThreadLocalRandom.current().nextLong();
        Long groupId = ThreadLocalRandom.current().nextLong();
        participantDao.create(new EditParticipantRequest(personId, groupId));

        // Act
        Participant actualResult = participantDao.findById(123L);

        // Assert
        assertThat(actualResult)
                .isNull();
    }
}
