package com.protvino.splitwise.adapter.impl;

import com.protvino.splitwise.IntegrationTest;
import com.protvino.splitwise._inmem.InMemoryUserDao;
import com.protvino.splitwise.adapter.UserDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;

@Tag("DAO")
public class UserDaoTest extends IntegrationTest {

    @Autowired
    private UserDao userDao;

    private final InMemoryUserDao inMemoryUserDao = new InMemoryUserDao();

    @Nested
    public class SqlDaoTest extends UsersDaoTestCases {
        @BeforeEach
        @AfterEach
        void setup() {
            clearTables("users");
        }

        @Override
        UserDao getDao() {
            return UserDaoTest.this.userDao;
        }
    }

    @Nested
    public class InMemoryDaoTest extends UsersDaoTestCases {
        @BeforeEach
        @AfterEach
        void setup(){
            UserDaoTest.this.inMemoryUserDao.clear();
        }

        @Override
        UserDao getDao() {
            return UserDaoTest.this.inMemoryUserDao;
        }
    }
}
