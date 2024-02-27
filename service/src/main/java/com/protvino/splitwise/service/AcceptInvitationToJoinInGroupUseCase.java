package com.protvino.splitwise.service;

import com.protvino.splitwise.adapter.ParticipantDao;
import com.protvino.splitwise.domain.request.EditParticipantRequest;
import com.protvino.splitwise.exceptions.ParticipantAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AcceptInvitationToJoinInGroupUseCase {

    private final ParticipantDao participantDao;

    public long acceptInvitation(Long personId, Long groupId) {
        // Проверить, что участник ещё не участвует, иначе бросить throw new Accept...Exception(), почитать про аннотации @ExceptionHandlers, @ResponseStatus(), отдать 409 статус в контроллере
        // Если всё ок, создаём участника
        // Дописать тесты на этот метод
        boolean participantNotExists = participantDao.checkIfExists(personId, groupId);
        if (participantNotExists) {
            return participantDao.create(new EditParticipantRequest(personId, groupId));
        } else {
            throw new ParticipantAlreadyExistsException("already exist");
        }
    }
}
