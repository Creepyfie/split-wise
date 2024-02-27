package com.protvino.splitwise.service;

import com.protvino.splitwise._inmem.InMemoryGroupDao;
import com.protvino.splitwise._inmem.InMemoryParticipantDao;
import com.protvino.splitwise._inmem.InMemoryPersonDao;
import com.protvino.splitwise.domain.value.Participant;
import com.protvino.splitwise.exceptions.ParticipantAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.protvino.splitwise.domain._data.EditGroupRequests.aGroup;
import static com.protvino.splitwise.domain._data.EditPersonRequests.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@Tag("UNIT")
class AcceptInvitationToJoinInGroupUseCaseTest {


    InMemoryPersonDao personDao = new InMemoryPersonDao();
    InMemoryGroupDao groupDao = new InMemoryGroupDao();
    InMemoryParticipantDao participantDao = new InMemoryParticipantDao();
    AcceptInvitationToJoinInGroupUseCase acceptInvitationToJoinInGroupUseCase = new AcceptInvitationToJoinInGroupUseCase(participantDao);

    @BeforeEach
    void setup() {
        personDao.clear();
        groupDao.clear();
        participantDao.clear();
    }

    @Test
    void accept_err__when_has_already_participated() {
        // Arrange
        long groupId = groupDao.create(aGroup());
        long personId = personDao.create(aPerson());

        // Act
        Throwable e = catchThrowable(() -> acceptInvitationToJoinInGroupUseCase.acceptInvitation(personId, groupId));

        // Assert
        assertThat(e).isInstanceOf(ParticipantAlreadyExistsException.class);
    }
    @Test
    void accept__when_has_no_already_participated() {
        // Arrange
        long groupId = groupDao.create(aGroup());
        long personId = personDao.create(aPerson());

        // Act
        long participantId = acceptInvitationToJoinInGroupUseCase.acceptInvitation(personId, groupId);

        // Assert
        Participant expectedParticipant = participantDao.findById(participantId);

        assertThat(expectedParticipant)
            .isEqualTo(new Participant(participantId,personId, groupId));
    }
}
