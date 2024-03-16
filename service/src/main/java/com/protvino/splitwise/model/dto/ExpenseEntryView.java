package com.protvino.splitwise.model.dto;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
@RequiredArgsConstructor
public class ExpenseEntryView {

    Long expenseId;
    Long payerParticipantId;
    Long debtorParticipantId;
    BigDecimal amount;
}
