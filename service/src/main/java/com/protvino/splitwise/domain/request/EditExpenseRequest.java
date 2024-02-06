package com.protvino.splitwise.domain.request;

import lombok.Value;

@Value
public class EditExpenseRequest {

    Long group_id;
    Long paying_participant_id;
    Double total;
    String comment;
}
