package com.protvino.splitwise.port.dto;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
@RequiredArgsConstructor
public class ExpenseDto {

    Long id;
    Long groupId;
    Long payingParticipantId;
    BigDecimal total;
    String comment;
}
