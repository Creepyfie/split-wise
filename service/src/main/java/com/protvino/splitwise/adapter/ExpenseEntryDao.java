package com.protvino.splitwise.adapter;

import com.protvino.splitwise.domain.request.EditDebtInExpenseRequest;
import com.protvino.splitwise.domain.value.ExpenseEntry;
import com.protvino.splitwise.model.DebtInExpenseView;
import com.protvino.splitwise.model.request.ExpenseEntrySearchRequest;

import java.util.List;

public interface ExpenseEntryDao {

    void create(EditDebtInExpenseRequest request);

    void update(EditDebtInExpenseRequest request);

    void updateBatch(List<EditDebtInExpenseRequest> requests);

    void delete(ExpenseEntry.Id id);

    ExpenseEntry findById(ExpenseEntry.Id id);
}
