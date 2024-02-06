package com.protvino.splitwise.domain.value;

import lombok.Value;

@Value
public class Debt_in_expense {

    Long expense_id;
    Long from_participant_id;
    Long to_participant_id;
    Double amount;
}
