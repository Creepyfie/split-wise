package com.protvino.splitwise.service;

import com.protvino.splitwise._inmem.InMemoryGroupDao;
import com.protvino.splitwise._inmem.InMemoryParticipantDao;
import com.protvino.splitwise._inmem.InMemoryPersonDao;
import com.protvino.splitwise.domain._data.EditGroupRequests;
import com.protvino.splitwise.domain._data.EditPersonRequests;
import com.protvino.splitwise.domain.request.EditGroupRequest;
import com.protvino.splitwise.domain.request.EditPersonRequest;
import com.protvino.splitwise.domain.value.Participant;
import org.assertj.core.api.Assertions;
import org.instancio.Instancio;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.protvino.splitwise.domain._data.EditGroupRequests.aGroup;
import static com.protvino.splitwise.domain._data.EditPersonRequests.*;
import static org.assertj.core.api.Assertions.*;

@Tag("UNIT")
class AcceptInvitationToJoinInGroupUseCaseTest {


    InMemoryPersonDao personDao = new InMemoryPersonDao();
    InMemoryGroupDao groupDao = new InMemoryGroupDao();
    InMemoryParticipantDao participantDao = new InMemoryParticipantDao();
    AcceptInvitationToJoinInGroupUseCase acceptInvitationToJoinInGroupUseCase = new AcceptInvitationToJoinInGroupUseCase(participantDao);

    @Test
    void create_err__when_has_already_participated() {
        // Arrange
        long groupId = groupDao.create(aGroup());
        Long personId = personDao.create(aPerson());

        // Act
        long participantId = acceptInvitationToJoinInGroupUseCase.acceptInvitation(personId, groupId);

        // Assert
        Participant expectedParticipant = participantDao.findById(participantId);
        assertThat(expectedParticipant)
            .isEqualTo(new Participant())
    }
}
