package com.protvino.splitwise.port;

import com.protvino.splitwise.domain.request.EditParticipantRequest;
import com.protvino.splitwise.service.AcceptInvitationToJoinInGroupUseCase;
import com.protvino.splitwise.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/participant")
@RequiredArgsConstructor
public class  ParticipantController {

    private final AcceptInvitationToJoinInGroupUseCase acceptInvitationUseCase;
    private final ScoreService scoreService;

    @PostMapping
    public long addParticipant(@RequestBody EditParticipantRequest participant) {
        return acceptInvitationUseCase.acceptInvitation(participant.getPersonId(), participant.getGroupId());
    }

    @GetMapping("/{participantId}")
    public Map<Long, BigDecimal> showScore(@RequestParam("participantId") long participantId) {
        return scoreService.showScoreRelativeToForeignParticipants(participantId);
    }
}
