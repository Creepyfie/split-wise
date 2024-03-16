package com.protvino.splitwise.domain.value;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;

@Value
@RequiredArgsConstructor(staticName = "of")
public class ExpenseEntry {

    Id id;
    BigDecimal amount;

    public Long getParticipantId() {
        return id.getParticipantId();
    }

    public Long getExpenseId () {
        return id.getExpenseId();
    }

    @Value
    @RequiredArgsConstructor(staticName = "of")
    public static class Id {
        Long expenseId;
        Long participantId;
    }
}
