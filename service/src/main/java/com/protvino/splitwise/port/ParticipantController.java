package com.protvino.splitwise.port;

import com.protvino.splitwise.adapter.ParticipantDao;
import com.protvino.splitwise.domain.request.EditParticipantRequest;
import com.protvino.splitwise.service.AcceptInvitationToJoinInGroupUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/participant")
@RequiredArgsConstructor
public class  ParticipantController {

    private final ParticipantDao participantDao;
    private final AcceptInvitationToJoinInGroupUseCase acceptInvitationUseCase;

    @PostMapping
    public long addParticipant(@RequestBody EditParticipantRequest participant) {
        return acceptInvitationUseCase.acceptInvitation(participant.getPersonId(), participant.getGroupId());
    }
}
