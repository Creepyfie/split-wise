package com.protvino.splitwise.model.request;

import lombok.Builder;
import lombok.Value;

import java.util.Set;

@Value
@Builder
public class ExpenseEntrySearchRequest {

    Set<Long> expenseIds;
    Set<Long> payerParticipantIds;
    Set<Long> debtorParticipantIds;
}
