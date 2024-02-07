package com.protvino.splitwise.domain.request;

import lombok.Value;

@Value
public class EditExpenseRequest {

    Long groupId;
    Long payingParticipantId;
    Double total;
    String comment;
}
