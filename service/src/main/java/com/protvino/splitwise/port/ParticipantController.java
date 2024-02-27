package com.protvino.splitwise.port;

import com.protvino.splitwise.adapter.ParticipantDao;
import com.protvino.splitwise.domain.request.EditParticipantRequest;
import com.protvino.splitwise.service.AcceptInvitationToJoinInGroupUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @GetMapping("/{id}")
    public Map<Long, Double> showDebts(@RequestParam("id") long id) {
        return null;
    }
}
