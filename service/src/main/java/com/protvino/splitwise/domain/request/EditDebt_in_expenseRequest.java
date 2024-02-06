package com.protvino.splitwise.domain.request;

import lombok.Value;

@Value
public class EditDebt_in_expenseRequest {

    Long expense_id;
    Long from_participant_id;
    Long to_participant_id;
    Double amount;
}
