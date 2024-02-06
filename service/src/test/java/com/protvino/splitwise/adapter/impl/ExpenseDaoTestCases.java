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
        Long groupId = 123L;
        Long paying_participantId = 123L;
        Double total = 234d;
        String comment = "Comment";

        // Act
        long actualId = expenseDao.create(new EditExpenseRequest(groupId,paying_participantId,total,comment));
        Expense actualResult = expenseDao.findById(actualId);

        // Assert
        Expense expectedResult = new Expense(actualId, groupId,paying_participantId,total,comment);

        assertThat(actualResult)
            .isEqualTo(expectedResult);
    }

    @Test
    void update__when_exists() {
        // Arrange
        Long groupId = 123L;
        Long paying_participantId = 123L;
        Double total = 234d;
        String comment = "Comment";
        long actualId = expenseDao.create(new EditExpenseRequest(groupId,paying_participantId,total,comment));

        // Act
        Long updateGroupId = 123L;
        Long updatePaying_participantId = 123L;
        Double updateTotal = 234d;
        String updateComment = "Comment";
        expenseDao.update(actualId, new EditExpenseRequest(updateGroupId
            ,updatePaying_participantId
            ,updateTotal
            ,updateComment));

        Expense actualResult = expenseDao.findById(actualId);

        // Assert
        Expense expectedResult = new Expense(actualId, updateGroupId
            ,updatePaying_participantId
            ,updateTotal
            ,updateComment);

        assertThat(actualResult)
            .isEqualTo(expectedResult);
    }

    @Test
    void update__when_does_not_exist() {
        // Arrange
        // Act
        Long updateGroupId = 123L;
        Long updatePaying_participantId = 123L;
        Double updateTotal = 234d;
        String updateComment = "Comment";
        expenseDao.update(666L, new EditExpenseRequest(updateGroupId
            ,updatePaying_participantId
            ,updateTotal
            ,updateComment));

        Expense actualResult = expenseDao.findById(666L);

        // Assert
        assertThat(actualResult)
            .isNull();
    }

    @Test
    void find_byId__when_exists() {
        // Arrange
        Long groupId = 123L;
        Long paying_participantId = 123L;
        Double total = 234d;
        String comment = "Comment";
        long actualId = expenseDao.create(new EditExpenseRequest(groupId,paying_participantId,total,comment));

        // Act
        Expense actualResult = expenseDao.findById(actualId);

        // Assert
        Expense expectedResult = new Expense(actualId, groupId,paying_participantId,total,comment);

        assertThat(actualResult)
            .isEqualTo(expectedResult);
    }

    @Test
    void find_byId__when_does_not_exist() {
        // Arrange
        Long groupId = 123L;
        Long paying_participantId = 123L;
        Double total = 234d;
        String comment = "Comment";
        expenseDao.create(new EditExpenseRequest(groupId,paying_participantId,total,comment));

        // Act
        Expense actualResult = expenseDao.findById(123L);

        // Assert
        assertThat(actualResult)
            .isNull();
    }
}
