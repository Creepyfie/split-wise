package com.protvino.splitwise.port;

import com.protvino.splitwise.adapter.impl.SqlParticipantDao;
import com.protvino.splitwise.domain.request.EditParticipantRequest;
import com.protvino.splitwise.exceptions.ParticipantAlreadyExistsException;
import com.protvino.splitwise.service.AcceptInvitationToJoinInGroupUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/participant")
@RequiredArgsConstructor
public class ParticipantController {

    private final SqlParticipantDao sqlParticipantDao;
    private final AcceptInvitationToJoinInGroupUseCase aCase;

    @PostMapping
    public long addParticipant(@RequestBody EditParticipantRequest participant){
        /*long id = aCase.acceptInvitation(participant.getPersonId(),participant.getGroupId());
        if (id == 0L)
        throw new ParticipantAlreadyExistsException("Участник уже существует");
        else
        return id;*/
        return aCase.acceptInvitation(participant.getPersonId(),participant.getGroupId());
    }
}
