package com.protvino.splitwise.adapter;

import com.protvino.splitwise.domain.request.EditDebtInExpenseRequest;
import com.protvino.splitwise.domain.value.DebtInExpense;

import java.util.List;

public interface Debt_in_expenseDao {

    void create(EditDebtInExpenseRequest request);

    void update(EditDebtInExpenseRequest request);

    void delete(EditDebtInExpenseRequest request);

    List<DebtInExpense> findByExpense_id(Long expense_id);
}
