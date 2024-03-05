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

import java.util.*;

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
                            .filter(p -> p.getToParticipantId() != id)
                            .forEach(debt ->
                                    balancesWithOther.merge(debt.getFromParticipantId(),debt.getAmount(),
                                            (k,v) -> v = v + debt.getAmount())
                            ));

        return balancesWithOther;
    }

    public Map<Long, Double> showAllBalances(long groupId) {

        List<Participant> groupParticipants = participantDao.findByGroupId(groupId);
        Map<Long, Double> participantsBalances = new HashMap<>();

        groupParticipants.forEach(participant -> {
            double balance = 0d;
            for (Double debt : showDebtsToOtherParticipants(participant.getId())
                    .values()) {
                balance = +debt;
            }
            participantsBalances.put(participant.getId(), balance);
        });
        return participantsBalances;
    }

    public Map<Long, Map<Long, Double>> refactorDebts(long groupId) {

        List<Participant> groupParticipants = participantDao.findByGroupId(groupId);
        Map<Long, Map<Long, Double>> allParticipantToOtherParticipantsBalancesInGroup = new HashMap<>();

        groupParticipants.forEach(participant -> {
            allParticipantToOtherParticipantsBalancesInGroup.put(participant.getId(), showDebtsToOtherParticipants(participant.getId()));
        });

        if (groupParticipants.size() > 2) {

            groupParticipants.forEach(participant -> {

                Map<Long,Double> participantDebts = allParticipantToOtherParticipantsBalancesInGroup.get(participant.getId());
                List<Long> positiveDebtId = new ArrayList<>();
                List<Long> negativeDebtId = new ArrayList<>();


                participantDebts.forEach((id, debt) -> {
                    if (debt > 0d) {
                        positiveDebtId.add(id);
                    } else
                        negativeDebtId.add(id);
                });

                negativeDebtId.forEach(negativeId -> {

                    Map<Long,Double> negIdParticipantDebts = allParticipantToOtherParticipantsBalancesInGroup.get(negativeId);

                    positiveDebtId.forEach(positiveId -> {

                        Map<Long,Double> posIdParticipantDebts = allParticipantToOtherParticipantsBalancesInGroup.get(positiveId);
                        Double negativeDebt = participantDebts.get(negativeId);
                        Double positiveDebt = participantDebts.get(positiveId);

                        if(positiveDebt + negativeDebt > 0d) {

                            participantDebts.put(negativeId, 0d);
                            participantDebts.put(positiveId,positiveDebt + negativeDebt);

                            negIdParticipantDebts.put(participant.getId(), 0d);
                            negIdParticipantDebts.put(positiveId, negIdParticipantDebts.get(positiveId) - negativeDebt);

                            posIdParticipantDebts.put(participant.getId(), posIdParticipantDebts.get(participant.getId()) - negativeDebt);
                            posIdParticipantDebts.put(negativeId, posIdParticipantDebts.get(negativeId) + negativeDebt);
                        } else {

                            participantDebts.put(negativeId, negativeDebt + positiveDebt);
                            participantDebts.put(positiveId,0d);

                            negIdParticipantDebts.put(participant.getId(),-negativeDebt - positiveDebt);
                            negIdParticipantDebts.put(positiveId, negIdParticipantDebts.get(positiveId) + positiveDebt);

                            posIdParticipantDebts.put(participant.getId(), 0d);
                            posIdParticipantDebts.put(negativeId, posIdParticipantDebts.get(negativeId) - positiveDebt);
                        }

                        allParticipantToOtherParticipantsBalancesInGroup.put(participant.getId(), participantDebts);
                        allParticipantToOtherParticipantsBalancesInGroup.put(negativeId,negIdParticipantDebts);
                        allParticipantToOtherParticipantsBalancesInGroup.put(positiveId,posIdParticipantDebts);
                    });
                });
            });
        }
        return allParticipantToOtherParticipantsBalancesInGroup;
    }
}
