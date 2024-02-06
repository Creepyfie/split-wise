package com.protvino.splitwise.service;

import com.protvino.splitwise.adapter.ParticipantDao;
import com.protvino.splitwise.domain.request.EditParticipantRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AcceptInvitationToJoinInGroupUseCase {

    private final ParticipantDao participantDao;

    public long acceptInvitation(Long personId, Long groupId) {
        // Проверить, что участник ещё не участвует, иначе бросить throw new Accept...Exception(), почитать про аннотации @ExceptionHandlers, @ResponseStatus(), отдать 409 статус в контроллере
        participantDao.checkForExists(personId, groupId);
        // Если всё ок, создаём участника
        // Дописать тесты на этот метод
        return participantDao.create(new EditParticipantRequest(personId, groupId));
    }
}
