package com.protvino.splitwise._inmem;

import com.protvino.splitwise.adapter.ExpenseEntryDao;
import com.protvino.splitwise.domain.request.EditDebtInExpenseRequest;
import com.protvino.splitwise.domain.value.ExpenseEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class InMemoryExpenseEntryInExpenseDao implements ExpenseEntryDao {

    Map<Long, ExpenseEntry> debts = new HashMap<>();

    @Override
    public void create(EditDebtInExpenseRequest request) {

        long id = ThreadLocalRandom.current().nextLong(0, 100000);

        debts.put(id,new ExpenseEntry(request.getExpenseId(), request.getFromParticipantId()
        , request.getToParticipantId(), request.getAmount()));
    }

    @Override
    public void update(EditDebtInExpenseRequest request) {


        if(debts.containsKey(1l)){
            debts.put(1l,new ExpenseEntry(request.getExpenseId(), request.getFromParticipantId()
                    , request.getToParticipantId(), request.getAmount()));
        }
    }

    @Override
    public void updateBatch(List<EditDebtInExpenseRequest> requests) {

    }

    @Override
    public void delete(EditDebtInExpenseRequest request) {

        debts.remove(1l);
    }

    @Override
    public List<ExpenseEntry> findByExpenseId(Long expenseId) {

        List<ExpenseEntry> result = new ArrayList<>();

        for (Map.Entry<Long, ExpenseEntry> debt: debts.entrySet()
             ) {
            if (debt.getValue().getExpenseId() == expenseId)
                result.add(debt.getValue());
        }
        return result;
    }

    @Override
    public List<ExpenseEntry> findByFromId(Long fromId) {
        return debts.values()
                .stream()
                .filter(debt -> debt.getParticipantId() == fromId)
                .toList();
    }
}
