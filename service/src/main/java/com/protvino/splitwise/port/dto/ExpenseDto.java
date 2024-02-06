package com.protvino.splitwise.port.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ExpenseDto {

    Long id;
    Long groupId;
    Long paying_participantId;
    Double total;
    String comment;
}
