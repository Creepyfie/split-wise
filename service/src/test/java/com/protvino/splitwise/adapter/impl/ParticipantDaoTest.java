package com.protvino.splitwise.adapter.impl;

import com.protvino.splitwise.IntegrationTest;
import com.protvino.splitwise.adapter.ParticipantDao;
import com.protvino.splitwise._inmem.InMemoryParticipantDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;

@Tag("DAO")
public class ParticipantDaoTest extends IntegrationTest {

    @Autowired
    private ParticipantDao participantDao;

    private final InMemoryParticipantDao inMemoryParticipantDao = new InMemoryParticipantDao();

    @Nested
    class SqlDaoTest extends ParticipantDaoTestCases{
        @BeforeEach
        @AfterEach
        void setup() {
            clearTables("participants");
        }
        @Override
        ParticipantDao getDao() {
            return ParticipantDaoTest.this.participantDao;
        }
    }

    @Nested
    class InMemDaoTest extends ParticipantDaoTestCases{
        @BeforeEach
        @AfterEach
        void setup(){
            ParticipantDaoTest.this.inMemoryParticipantDao.clear();
        }
        @Override
        ParticipantDao getDao() {
            return ParticipantDaoTest.this.inMemoryParticipantDao;
        }
    }

}
