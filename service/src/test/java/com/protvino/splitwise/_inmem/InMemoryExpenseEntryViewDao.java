package com.protvino.splitwise._inmem;

import com.protvino.splitwise.adapter.ExpenseEntryViewDao;
import com.protvino.splitwise.domain.value.ExpenseEntry;
import com.protvino.splitwise.model.dto.ExpenseEntryView;
import com.protvino.splitwise.model.request.ExpenseEntrySearchRequest;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.collections4.CollectionUtils.*;

@RequiredArgsConstructor
public class InMemoryExpenseEntryViewDao implements ExpenseEntryViewDao {

    private final InMemoryExpenseDao expenseDao;

    Map<Long, ExpenseEntry> rows = new HashMap<>();

    @Override
    public List<ExpenseEntryView> search(ExpenseEntrySearchRequest request) {
        expenseDao.findById()
        return rows.values()
            .stream()
            .filter(row -> isEmpty(request.getExpenseIds())
                || request.getExpenseIds().contains(row.getExpenseId()))
            .filter(row -> isEmpty(request.getPayerParticipantIds())
                || request.getPayerParticipantIds().contains(row.getPa()))
            .filter(row -> isEmpty(request.getDebtorParticipantIds())
                || request.getDebtorParticipantIds().contains(row.getParticipantId()))
    }

    @Override
    public List<ExpenseEntryView> findDebtorEntriesInForeignExpenses(Long debtorParticipantId) {
        return null;
    }

    @Override
    public List<ExpenseEntryView> findForeignEntriesInPayerExpenses(Long payerParticipantId) {
        return null;
    }
}
