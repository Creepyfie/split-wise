package com.protvino.splitwise.port.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ExpenseDto {

    Long id;
    Long group_id;
    Long paying_participant_id;
    Double total;
    String comment;
}
