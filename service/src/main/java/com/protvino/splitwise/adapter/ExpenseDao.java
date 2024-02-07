package com.protvino.splitwise.adapter;

import com.protvino.splitwise.domain.request.EditExpenseRequest;
import com.protvino.splitwise.domain.value.Expense;

import java.util.List;

public interface ExpenseDao {

    long create(EditExpenseRequest editExpenseRequest);

    void update(long id,EditExpenseRequest editExpenseRequest);

    Expense findById(long id);

    List<Expense> findByGroupId(long groupId);
}
