package com.protvino.splitwise.domain.request;

import lombok.Value;

@Value
public class EditExpenseRequest {

    Long payingParticipantId;
    BigDecimal total;
    String comment;
}
