package com.protvino.splitwise.domain.value;

import lombok.Value;

@Value
public class Expense {

    Long id;
    Long paying_participantId;
    BigDecimal total;
    String comment;
}
