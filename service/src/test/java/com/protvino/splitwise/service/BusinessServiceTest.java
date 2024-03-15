package com.protvino.splitwise.service;

import com.protvino.splitwise._inmem.InMemoryDebtInExpenseDao;
import com.protvino.splitwise._inmem.InMemoryGroupDao;
import com.protvino.splitwise._inmem.InMemoryParticipantDao;
import com.protvino.splitwise.domain.request.EditDebtInExpenseRequest;
import com.protvino.splitwise.domain.request.EditGroupRequest;
import com.protvino.splitwise.domain.request.EditParticipantRequest;
import com.protvino.splitwise.domain.request.EditUserRequest;
import com.protvino.splitwise.exceptions.UserAlreadyExistsExeption;
import org.assertj.core.api.Long2DArrayAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@Tag("UNIT")
public class BusinessServiceTest {

    private final InMemoryGroupDao inMemoryGroupDao = new InMemoryGroupDao();
    private final InMemoryParticipantDao inMemoryParticipantDao = new InMemoryParticipantDao();
    private final InMemoryDebtInExpenseDao inMemoryDebtInExpenseDao = new InMemoryDebtInExpenseDao();

    BusinessService businessService = new BusinessService(inMemoryParticipantDao,inMemoryDebtInExpenseDao);


    @Test
    void show_Debts_To_Other_Participants_test() {
        // Arrange
        String groupName = "GroupName";
        Long personId1 = 111L;
        Long personId2 = 222L;
        Long personId3 = 333L;
        Long expenseId = 444L;
        BigDecimal amount1 = 100d;
        BigDecimal amount2 = 200d;

        // Act
        Long groupId = inMemoryGroupDao.create(new EditGroupRequest(groupName));
        Long participantId1 = inMemoryParticipantDao.create(new EditParticipantRequest(personId1,groupId));
        Long participantId2 = inMemoryParticipantDao.create(new EditParticipantRequest(personId2,groupId));
        Long participantId3 = inMemoryParticipantDao.create(new EditParticipantRequest(personId3,groupId));

        inMemoryDebtInExpenseDao.create(new EditDebtInExpenseRequest(expenseId,participantId1, participantId2, amount1));
        inMemoryDebtInExpenseDao.create(new EditDebtInExpenseRequest(expenseId,participantId3, participantId2,amount2));
        inMemoryDebtInExpenseDao.create(new EditDebtInExpenseRequest(expenseId,participantId2, participantId2,0d));

        // Assert
        Map<Long, BigDecimal> result = businessService.showAllBalances(groupId);

        Map<Long, BigDecimal> expected = new HashMap<>();
        expected.put(participantId1,amount1);
        expected.put(participantId3,amount2);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void show_all_balances_test() {
        // Arrange
        String groupName = "GroupName";
        Long personId1 = 111L;
        Long personId2 = 222L;
        Long personId3 = 333L;
        Long expenseId = 444L;
        BigDecimal amount1 = 100d;
        BigDecimal amount2 = 200d;

        // Act
        Long groupId = inMemoryGroupDao.create(new EditGroupRequest(groupName));
        Long participantId1 = inMemoryParticipantDao.create(new EditParticipantRequest(personId1,groupId));
        Long participantId2 = inMemoryParticipantDao.create(new EditParticipantRequest(personId2,groupId));
        Long participantId3 = inMemoryParticipantDao.create(new EditParticipantRequest(personId3,groupId));

        inMemoryDebtInExpenseDao.create(new EditDebtInExpenseRequest(expenseId,participantId1, participantId2, amount1));
        inMemoryDebtInExpenseDao.create(new EditDebtInExpenseRequest(expenseId,participantId3, participantId2,amount2));
        inMemoryDebtInExpenseDao.create(new EditDebtInExpenseRequest(expenseId,participantId2, participantId2,0d));

        // Assert
        Map<Long, BigDecimal> result = businessService.showAllBalances(groupId);

        Map<Long, BigDecimal> expected = new HashMap<>();
        expected.put(participantId1,-amount1);
        expected.put(participantId2,amount1 + amount2);
        expected.put(participantId3,-amount2);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void refactor_debts_test() {
        // Arrange
        String groupName = "GroupName";
        Long personId1 = 111L;
        Long personId2 = 222L;
        Long personId3 = 333L;
        Long expenseId1 = 444L;
        Long expenseId2 = 555L;
        BigDecimal amount1 = 100d;
        BigDecimal amount2 = 200d;

        // Act
        Long groupId = inMemoryGroupDao.create(new EditGroupRequest(groupName));
        Long participantId1 = inMemoryParticipantDao.create(new EditParticipantRequest(personId1,groupId));
        Long participantId2 = inMemoryParticipantDao.create(new EditParticipantRequest(personId2,groupId));
        Long participantId3 = inMemoryParticipantDao.create(new EditParticipantRequest(personId3,groupId));

        inMemoryDebtInExpenseDao.create(new EditDebtInExpenseRequest(expenseId1,participantId1, participantId2, amount1));
        inMemoryDebtInExpenseDao.create(new EditDebtInExpenseRequest(expenseId2,participantId2, participantId3,amount2));

        // Assert
        Map<Long, Map<Long, BigDecimal>> result = businessService.refactorDebts(groupId);
        // Act
        Map<Long, Map<Long, BigDecimal>> expected = new HashMap<>();

        Map<Long, BigDecimal> partMap1 = new HashMap<>();
        partMap1.put(participantId2, 0d);
        partMap1.put(participantId3, -100d);
        expected.put(participantId1,partMap1);

        Map<Long, BigDecimal> partMap2 = new HashMap<>();
        partMap2.put(participantId1, 0d);
        partMap2.put(participantId3, -100d);
        expected.put(participantId2,partMap2);

        Map<Long, BigDecimal> partMap3 = new HashMap<>();
        partMap3.put(participantId1, 100d);
        partMap3.put(participantId2, 100d);
        expected.put(participantId3,partMap3);

        assertThat(result).isEqualTo(expected);
    }

}
