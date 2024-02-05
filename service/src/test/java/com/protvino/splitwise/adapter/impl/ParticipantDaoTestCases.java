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
        Long person_id = ThreadLocalRandom.current().nextLong();
        Long group_id = ThreadLocalRandom.current().nextLong();

        // Act
        Long actualId = participantDao.create(new EditParticipantRequest(person_id, group_id));

        Participant actualResult = participantDao.findById(actualId);

        // Assert
        Participant expectedResult = new Participant(actualId, person_id, group_id);

        assertThat(actualResult)
                .isEqualTo(expectedResult);
    }

    @Test
    void update__when_exists() {

        // Arrange
        Long person_id = ThreadLocalRandom.current().nextLong();
        Long group_id = ThreadLocalRandom.current().nextLong();
        Long actualId = participantDao.create(new EditParticipantRequest(person_id, group_id));

        // Act
        Long updatedPerson_id = ThreadLocalRandom.current().nextLong();
        Long updatedGroup_id = ThreadLocalRandom.current().nextLong();
        participantDao.update(actualId, new EditParticipantRequest(updatedPerson_id, updatedGroup_id));

        Participant actualResult = participantDao.findById(actualId);

        // Assert
        Participant expectedResult = new Participant(actualId, updatedPerson_id, updatedGroup_id);

        assertThat(actualResult)
                .isEqualTo(expectedResult);
    }

    @Test
    void update__when_does_not_exist() {
        // Arrange
        // Act
        Long updatedPerson_id = ThreadLocalRandom.current().nextLong();
        Long updatedGroup_id = ThreadLocalRandom.current().nextLong();
        participantDao.update(123L, new EditParticipantRequest(updatedPerson_id, updatedGroup_id));

        Participant actualResult = participantDao.findById(123L);

        // Assert
        assertThat(actualResult)
                .isNull();
    }

    @Test
    void find_by_id__when_exists() {
        // Arrange
        Long person_id = ThreadLocalRandom.current().nextLong();
        Long group_id = ThreadLocalRandom.current().nextLong();
        Long actualId = participantDao.create(new EditParticipantRequest(person_id, group_id));

        // Act
        Participant actualResult = participantDao.findById(actualId);

        // Assert
        Participant expectedResult = new Participant(actualId, person_id, group_id);

        assertThat(actualResult)
                .isEqualTo(expectedResult);
    }

    @Test
    void find_by_id__when_does_not_exist() {
        // Arrange
        Long person_id = ThreadLocalRandom.current().nextLong();
        Long group_id = ThreadLocalRandom.current().nextLong();
        Long actualId = participantDao.create(new EditParticipantRequest(person_id, group_id));

        // Act
        Participant actualResult = participantDao.findById(123L);

        // Assert
        assertThat(actualResult)
                .isNull();
    }
}
