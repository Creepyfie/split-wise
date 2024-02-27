package com.protvino.splitwise.adapter.impl;

import com.protvino.splitwise.IntegrationTest;
import com.protvino.splitwise.adapter.GroupDao;
import com.protvino.splitwise._inmem.InMemoryGroupDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;

@Tag("DAO")
public class GroupDaoTest extends IntegrationTest {
    @Autowired
    private GroupDao groupDAO;

    private final InMemoryGroupDao inMemoryGroupDao = new InMemoryGroupDao();

    @Nested
    class SqlDaoTest extends GroupDaoTestCases{
        @BeforeEach
        @AfterEach
        void setup() {
            clearTables("groups");
        }
        @Override
        GroupDao getDao() {
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
        GroupDao getDao() {
            return GroupDaoTest.this.inMemoryGroupDao;
        }
    }
}
