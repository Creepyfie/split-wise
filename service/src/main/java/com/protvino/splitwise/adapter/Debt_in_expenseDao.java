package com.protvino.splitwise.adapter;

import com.protvino.splitwise.domain.request.EditDebt_in_expenseRequest;
import com.protvino.splitwise.domain.request.EditExpenseRequest;
import com.protvino.splitwise.domain.value.Debt_in_expense;

import java.util.List;

public interface Debt_in_expenseDao {

    void create(EditDebt_in_expenseRequest request);

    void update(EditDebt_in_expenseRequest request);

    void delete(EditDebt_in_expenseRequest request);

    List<Debt_in_expense> findByExpense_id(Long expense_id);
}
