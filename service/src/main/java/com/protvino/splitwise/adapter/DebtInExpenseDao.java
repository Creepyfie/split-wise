package com.protvino.splitwise.adapter;

import com.protvino.splitwise.domain.request.EditDebtInExpenseRequest;
import com.protvino.splitwise.domain.value.DebtInExpense;

import java.util.List;

public interface DebtInExpenseDao {

    void create(EditDebtInExpenseRequest request);

    void update(EditDebtInExpenseRequest request);

    void update(List<EditDebtInExpenseRequest> requests);

    void delete(EditDebtInExpenseRequest request);

    List<DebtInExpense> findByExpenseId(Long expenseId);
}
