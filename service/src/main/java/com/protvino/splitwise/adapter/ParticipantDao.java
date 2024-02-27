package com.protvino.splitwise.adapter;

import com.protvino.splitwise.domain.request.EditParticipantRequest;
import com.protvino.splitwise.domain.value.Participant;

public interface ParticipantDao {

    long create(EditParticipantRequest editParticipantRequest);

    void update(long id, EditParticipantRequest editParticipantRequest);

    void delete(EditParticipantRequest editParticipantRequest);

    void delete(long id);

    Participant findById(Long id);

    boolean checkIfExists(long personId, long groupId);
}
