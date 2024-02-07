package com.protvino.splitwise.domain.request;

import lombok.Value;

@Value
public class EditDebtInExpenseRequest {

    Long expenseId;
    Long fromParticipantId;
    Long toParticipantId;
    Double amount;
}
