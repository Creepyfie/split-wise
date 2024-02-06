package com.protvino.splitwise.domain.request;

import lombok.Value;

@Value
public class EditDebtInExpenseRequest {

    Long expenseId;
    Long from_participantId;
    Long to_participantId;
    Double amount;
}
