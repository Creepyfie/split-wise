package com.protvino.splitwise._inmem;

import com.protvino.splitwise.adapter.ParticipantDao;
import com.protvino.splitwise.domain.request.EditParticipantRequest;
import com.protvino.splitwise.domain.value.Participant;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class InMemoryParticipantDao implements ParticipantDao {

    Map<Long,Participant> rows = new HashMap<>();

    @Override
    public long create(EditParticipantRequest editParticipantRequest) {

        long id = ThreadLocalRandom.current().nextLong(0, 100000);

        rows.put(id,new Participant(id,editParticipantRequest.getPersonId(), editParticipantRequest.getGroupId()));

        return id;
    }

    @Override
    public void update(long id, EditParticipantRequest editParticipantRequest) {

        if(rows.containsKey(id)){
            rows.put(id,new Participant(id,editParticipantRequest.getPersonId(),editParticipantRequest.getGroupId()));
        }
    }

    @Override
    public void delete(EditParticipantRequest editParticipantRequest) {
        for(Map.Entry<Long, Participant> participant: rows.entrySet()){
            if (participant.getValue().getGroupId() == editParticipantRequest.getGroupId()){
                rows.remove(participant.getKey());
            }
        }
    }

    @Override
    public void delete(long id) {
        rows.remove(id);
    }

    @Override
    public Participant findById(Long id) {
        return rows.get(id);
    }

    @Override
    public boolean checkIfNotExists(long personId, long groupId) {
        return rows.values()
            .stream()
            .noneMatch(it -> it.getGroupId() == groupId && it.getPersonId() == personId);
    }

    public void clear(){rows.clear();}
}
