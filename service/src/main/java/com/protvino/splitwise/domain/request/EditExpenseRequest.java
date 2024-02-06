package com.protvino.splitwise.domain.request;

import lombok.Value;

@Value
public class EditExpenseRequest {

    Long groupId;
    Long paying_participantId;
    Double total;
    String comment;
}
