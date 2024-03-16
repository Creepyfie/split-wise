package com.protvino.splitwise.domain.request;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class EditDebtInExpenseRequest {

    Long expenseId;
    Long participantId;
    BigDecimal amount;
}
