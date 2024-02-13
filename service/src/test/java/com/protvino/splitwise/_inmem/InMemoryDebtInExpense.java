package com.protvino.splitwise._inmem;

import com.protvino.splitwise.adapter.DebtInExpenseDao;
import com.protvino.splitwise.domain.request.EditDebtInExpenseRequest;
import com.protvino.splitwise.domain.value.DebtInExpense;
import org.testcontainers.shaded.com.google.common.hash.HashCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class InMemoryDebtInExpense implements DebtInExpenseDao {

    Map<Long,DebtInExpense> debts = new HashMap<>();

    @Override
    public void create(EditDebtInExpenseRequest request) {
        long id = 1l;
        debts.put(id,new DebtInExpense(request.getExpenseId(), request.getFromParticipantId()
        , request.getToParticipantId(), request.getAmount()));
    }

    @Override
    public void update(EditDebtInExpenseRequest request) {
        if(debts.);
    }

    @Override
    public void delete(EditDebtInExpenseRequest request) {

    }

    @Override
    public List<DebtInExpense> findByExpenseId(Long expenseId) {
        return null;
    }
}
