package com.protvino.splitwise.port;

import com.protvino.splitwise.port.dto.ExpenseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/expense")
public class ExpenseController {

    Map<Long, ExpenseDto> expenses = new HashMap<>();

    @GetMapping("/{id}")
    public ExpenseDto getExpense(@PathVariable("id") long id) {
        return expenses.get(id);
    }

    @GetMapping("/group/{group_id}")
    public List<ExpenseDto> getGroupExpenses(@PathVariable long group_id) {

        List<ExpenseDto> result = new ArrayList<>();

        for (Map.Entry<Long, ExpenseDto> entry : expenses.entrySet()) {
            ExpenseDto expense = entry.getValue();
            if (expense.getGroupId() == group_id)
                result.add(expense);
        }
        return result;
    }

    @PostMapping
    public Long createExpense() {
        ExpenseDto expense = ExpenseDto.builder()
            .id(ThreadLocalRandom.current().nextLong())
            .groupId(ThreadLocalRandom.current().nextLong())
            .payingParticipantId(ThreadLocalRandom.current().nextLong())
            .total(BigDecimal.valueOf(ThreadLocalRandom.current().nextInt()))
            .comment("Comment" + ThreadLocalRandom.current().nextInt(1000))
            .build();
        expenses.put(expense.getId(), expense);
        return expense.getId();
    }
}
