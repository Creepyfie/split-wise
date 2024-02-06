package com.protvino.splitwise.domain.value;

import lombok.Value;

@Value
public class Expense {

    Long id;
    Long group_id;
    Long paying_participant_id;
    Double total;
    String comment;
}
