package com.protvino.splitwise.adapter.impl;

import com.protvino.splitwise.IntegrationTest;
import com.protvino.splitwise._inmem.InMemoryExpenseDao;
import com.protvino.splitwise.adapter.ExpenseDao;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;

@Tag("DAO")
public class ExpenseDaoTest extends IntegrationTest {

    @Autowired
    private ExpenseDao expenseDao;
    private final InMemoryExpenseDao inMemoryExpenseDao = new InMemoryExpenseDao();

    @Nested
    class SqlDaoTest extends ExpenseDaoTestCases{
        @BeforeEach
        @AfterEach
        void setup() {
            clearTables("expenses");
        }
        @Override
        ExpenseDao getDao() {
            return ExpenseDaoTest.this.expenseDao;
        }
    }

    @Nested
    class InMemDaoTest extends ExpenseDaoTestCases{
        @BeforeEach
        @AfterEach
        void setup(){
            ExpenseDaoTest.this.inMemoryExpenseDao.clear();
        }
        @Override
        ExpenseDao getDao() {
            return ExpenseDaoTest.this.inMemoryExpenseDao;
        }
    }
}
