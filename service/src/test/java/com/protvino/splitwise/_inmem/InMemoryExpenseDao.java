package com.protvino.splitwise._inmem;

import com.protvino.splitwise.adapter.ExpenseDao;
import com.protvino.splitwise.domain.request.EditExpenseRequest;
import com.protvino.splitwise.domain.value.Expense;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class InMemoryExpenseDao implements ExpenseDao {

    Map<Long, Expense> expenses = new HashMap<>();

    @Override
    public long create(EditExpenseRequest editExpenseRequest) {

        long id = ThreadLocalRandom.current().nextLong(0, 100000);

        expenses.put(id, new Expense(id, editExpenseRequest.getPayingParticipantId(),
            editExpenseRequest.getTotal(),
            editExpenseRequest.getComment()));
        return id;
    }

    @Override
    public void update(long id, EditExpenseRequest editExpenseRequest) {
        if(expenses.containsKey(id)){
            expenses.put(id, new Expense(id,editExpenseRequest.getPayingParticipantId(),
                editExpenseRequest.getTotal(),
                editExpenseRequest.getComment()));
        }
    }

    @Override
    public Expense findById(long id) {
        return expenses.get(id);
    }

    public void clear() {
        expenses.clear();
    }
}
