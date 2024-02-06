package com.protvino.splitwise.adapter.impl;

import com.protvino.splitwise.adapter.ExpenseDao;
import com.protvino.splitwise.adapter.PersonDao;
import com.protvino.splitwise.domain.request.EditExpenseRequest;
import com.protvino.splitwise.domain.request.EditPersonRequest;
import com.protvino.splitwise.domain.value.Expense;
import com.protvino.splitwise.domain.value.Person;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class ExpenseDaoTestCases {

    abstract ExpenseDao getDao();

    ExpenseDao expenseDao = getDao();

    @Test
    void create__when_does_not_exist() {
        // Arrange
        Long group_id = 123L;
        Long paying_participant_id = 123L;
        Double total = 234d;
        String comment = "Comment";

        // Act
        long actualId = expenseDao.create(new EditExpenseRequest(group_id,paying_participant_id,total,comment));
        Expense actualResult = expenseDao.findById(actualId);

        // Assert
        Expense expectedResult = new Expense(actualId, group_id,paying_participant_id,total,comment);

        assertThat(actualResult)
            .isEqualTo(expectedResult);
    }

    @Test
    void update__when_exists() {
        // Arrange
        Long group_id = 123L;
        Long paying_participant_id = 123L;
        Double total = 234d;
        String comment = "Comment";
        long actualId = expenseDao.create(new EditExpenseRequest(group_id,paying_participant_id,total,comment));

        // Act
        Long updateGroup_id = 123L;
        Long updatePaying_participant_id = 123L;
        Double updateTotal = 234d;
        String updateComment = "Comment";
        expenseDao.update(actualId, new EditExpenseRequest(updateGroup_id
            ,updatePaying_participant_id
            ,updateTotal
            ,updateComment));

        Expense actualResult = expenseDao.findById(actualId);

        // Assert
        Expense expectedResult = new Expense(actualId, updateGroup_id
            ,updatePaying_participant_id
            ,updateTotal
            ,updateComment);

        assertThat(actualResult)
            .isEqualTo(expectedResult);
    }

    @Test
    void update__when_does_not_exist() {
        // Arrange
        // Act
        Long updateGroup_id = 123L;
        Long updatePaying_participant_id = 123L;
        Double updateTotal = 234d;
        String updateComment = "Comment";
        expenseDao.update(666L, new EditExpenseRequest(updateGroup_id
            ,updatePaying_participant_id
            ,updateTotal
            ,updateComment));

        Expense actualResult = expenseDao.findById(666L);

        // Assert
        assertThat(actualResult)
            .isNull();
    }

    @Test
    void find_by_id__when_exists() {
        // Arrange
        Long group_id = 123L;
        Long paying_participant_id = 123L;
        Double total = 234d;
        String comment = "Comment";
        long actualId = expenseDao.create(new EditExpenseRequest(group_id,paying_participant_id,total,comment));

        // Act
        Expense actualResult = expenseDao.findById(actualId);

        // Assert
        Expense expectedResult = new Expense(actualId, group_id,paying_participant_id,total,comment);

        assertThat(actualResult)
            .isEqualTo(expectedResult);
    }

    @Test
    void find_by_id__when_does_not_exist() {
        // Arrange
        Long group_id = 123L;
        Long paying_participant_id = 123L;
        Double total = 234d;
        String comment = "Comment";
        expenseDao.create(new EditExpenseRequest(group_id,paying_participant_id,total,comment));

        // Act
        Expense actualResult = expenseDao.findById(123L);

        // Assert
        assertThat(actualResult)
            .isNull();
    }
}
