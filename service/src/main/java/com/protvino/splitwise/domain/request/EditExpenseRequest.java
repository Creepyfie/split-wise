package com.protvino.splitwise.domain.request;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class EditExpenseRequest {

    Long id;
    Long payingParticipantId;
    BigDecimal total;
    String comment;
}
