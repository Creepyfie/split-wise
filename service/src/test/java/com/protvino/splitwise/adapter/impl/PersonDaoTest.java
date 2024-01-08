package com.protvino.splitwise.adapter.impl;

import com.protvino.splitwise.IntegrationTest;
import com.protvino.splitwise.adapter.PersonDao;
import com.protvino.splitwise.inmem.InMemoryPersonDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;

@Tag("DAO")
public class PersonDaoTest extends IntegrationTest {

    // Инициализация обычного Дао через спринг
    @Autowired
    private PersonDao personDao;

    // Инициализация in memory Дао вручную
    private final InMemoryPersonDao inMemoryPersonDao = new InMemoryPersonDao();

    @Nested
    public class SqlDaoTest extends PersonDaoTestCases {
        @BeforeEach
        @AfterEach
        void setup() {
            clearTables("persons");
        }

        @Override
        PersonDao getDao() {
            return PersonDaoTest.this.personDao;
        }
    }

    @Nested
    public class InMemoryDaoTest extends PersonDaoTestCases {
        @BeforeEach
        @AfterEach
        void setup(){
            PersonDaoTest.this.inMemoryPersonDao.clear();
        }

        @Override
        PersonDao getDao() {
            return PersonDaoTest.this.inMemoryPersonDao;
        }
    }
}
