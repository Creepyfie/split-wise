package com.protvino.splitwise.domain.value;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class Expense {

    Long id;
    Long payingParticipantId;
    BigDecimal total;
    String comment;
}
