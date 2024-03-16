package com.protvino.splitwise.adapter;

import com.protvino.splitwise.domain.value.ExpenseEntry;
import com.protvino.splitwise.model.dto.ExpenseEntryView;
import com.protvino.splitwise.model.request.ExpenseEntrySearchRequest;

import java.util.List;

public interface ExpenseEntryViewDao {

    List<ExpenseEntryView> search(ExpenseEntrySearchRequest request);

    List<ExpenseEntryView> findDebtorEntriesInForeignExpenses(Long debtorParticipantId);

    List<ExpenseEntryView> findForeignEntriesInPayerExpenses(Long payerParticipantId);
}
