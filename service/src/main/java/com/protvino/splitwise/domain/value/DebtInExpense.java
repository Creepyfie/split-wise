package com.protvino.splitwise.domain.value;

import lombok.Value;

@Value
public class DebtInExpense {

    Long expenseId;
    Long from_participantId;
    Long to_participantId;
    Double amount;
}
