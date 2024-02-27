package com.protvino.splitwise.domain.value;

import lombok.Value;

@Value
public class DebtInExpense {

    Long expenseId;
    Long fromParticipantId;
    Long toParticipantId;
    Double amount;
}
