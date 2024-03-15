package com.protvino.splitwise.domain.value;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class DebtInExpense {

    Long expenseId;
    Long participantId;
    BigDecimal amount;
}
