package com.protvino.splitwise.model;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
@RequiredArgsConstructor
public class DebtInExpenseView {

    Long participantId;

    Long payingParticipantId;

    BigDecimal amount;
}
