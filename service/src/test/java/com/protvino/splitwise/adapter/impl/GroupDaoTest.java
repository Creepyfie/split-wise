package com.protvino.splitwise.adapter.impl;

import com.protvino.splitwise.IntegrationTest;
import com.protvino.splitwise.adapter.GroupDAO;
import com.protvino.splitwise.inmem.InMemoryGroupDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;

@Tag("DAO")
public class GroupDaoTest extends IntegrationTest {
    @Autowired
    private GroupDAO groupDAO;

    private final InMemoryGroupDao inMemoryGroupDao = new InMemoryGroupDao();

    @Nested
    class SqlDaoTest extends GroupDaoTestCases{
        @BeforeEach
        @AfterEach
        void setup() {
            clearTables("persons");
        }
        @Override
        GroupDAO getDAO() {
            return GroupDaoTest.this.groupDAO;
        }
    }

    @Nested
    class InMemDaoTest extends GroupDaoTestCases{
        @BeforeEach
        @AfterEach
        void setup(){
            GroupDaoTest.this.inMemoryGroupDao.clear();
        }
        @Override
        GroupDAO getDAO() {
            return GroupDaoTest.this.inMemoryGroupDao;
        }
    }
}
