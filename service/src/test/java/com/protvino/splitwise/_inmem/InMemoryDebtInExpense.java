package com.protvino.splitwise._inmem;

import com.protvino.splitwise.adapter.DebtInExpenseDao;
import com.protvino.splitwise.domain.request.EditDebtInExpenseRequest;
import com.protvino.splitwise.domain.value.DebtInExpense;
import org.testcontainers.shaded.com.google.common.hash.HashCode;

import java.util.ArrayList;
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

        if(debts.containsKey(1l)){
            debts.put(1l,new DebtInExpense(request.getExpenseId(), request.getFromParticipantId()
                    , request.getToParticipantId(), request.getAmount()));
        }
    }

    @Override
    public void delete(EditDebtInExpenseRequest request) {

        debts.remove(1l);
    }

    @Override
    public List<DebtInExpense> findByExpenseId(Long expenseId) {

        List<DebtInExpense> result = new ArrayList<>();

        for (Map.Entry<Long,DebtInExpense> debt: debts.entrySet()
             ) {
            if (debt.getValue().getExpenseId() == expenseId)
                result.add(debt.getValue());
        }
        return result;
    }
}
