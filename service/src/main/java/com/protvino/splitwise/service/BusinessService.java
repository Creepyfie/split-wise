package com.protvino.splitwise.service;


import com.protvino.splitwise.adapter.DebtInExpenseDao;
import com.protvino.splitwise.adapter.ParticipantDao;
import com.protvino.splitwise.domain.value.DebtInExpense;
import com.protvino.splitwise.domain.value.Participant;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class BusinessService {

    private final NamedParameterJdbcOperations jdbc;
    private final ParticipantDao participantDao;
    private final DebtInExpenseDao debtInExpenseDao;

    public Map<Long, Double> showDebtsToOtherParticipants(long id) {

        Map<Long, Double> balancesWithOther = new HashMap<>();

        Participant participantInfo = participantDao.findById(id);

        List<Participant> groupParticipants = participantDao.findByGroupId(participantInfo.getGroupId());

        debtInExpenseDao.findByFromId(id)
                .stream()
                .forEach(debt ->
                    balancesWithOther.merge(debt.getToParticipantId(),-debt.getAmount(),(k,v) -> v = v - debt.getAmount())
                );

        groupParticipants.stream()
                .forEach(participant ->
                    debtInExpenseDao.findByFromId(participant.getId())
                            .stream()
                            .filter(p -> p.getToParticipantId() == id)
                            .forEach(debt ->
                                    balancesWithOther.merge(debt.getFromParticipantId(),debt.getAmount(),
                                            (k,v) -> v = v + debt.getAmount())
                            ));

        return balancesWithOther;
    }

    public void refactorDebts() {

    }
}
