package com.protvino.splitwise.service;


import com.protvino.splitwise.adapter.ExpenseEntryDao;
import com.protvino.splitwise.adapter.ParticipantDao;
import com.protvino.splitwise.domain.value.ExpenseEntry;
import com.protvino.splitwise.domain.value.Participant;
import com.protvino.splitwise.model.DebtInExpenseView;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.SetUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.MoreObjects.firstNonNull;
import static java.math.BigDecimal.ZERO;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.reducing;

@Service
@RequiredArgsConstructor
public class ScoreService {

    private final ParticipantDao participantDao;
    private final ExpenseEntryDao expenseEntryDao;


    /**
     * Возвращает список моих участий (debts) в чужих тратах (expenses).
     * key - participantId, value - debt
     */
    public Map<Long, BigDecimal> showScoreRelativeToForeignParticipants(long participantId) {

        Map<Long, BigDecimal> participantExpenseEntriesByPayerId = findParticipantExpenseEntries(participantId);
        Map<Long, BigDecimal> foreignExpenseEntriesByParticipantId = findForeignExpenseEntries(participantId);

        Map<Long, BigDecimal> scoreRelativeForeigners = new HashMap<>();

        for (Long foreignParticipantId : SetUtils.union(participantExpenseEntriesByPayerId.keySet(), foreignExpenseEntriesByParticipantId.keySet())) {

            BigDecimal positiveScore = firstNonNull(foreignExpenseEntriesByParticipantId.get(foreignParticipantId), ZERO);
            BigDecimal negativeScore = firstNonNull(participantExpenseEntriesByPayerId.get(foreignParticipantId), ZERO);

            BigDecimal totalScore = positiveScore.subtract(negativeScore);

            scoreRelativeForeigners.put(foreignParticipantId, totalScore);
        }

        return scoreRelativeForeigners;
    }

    private Map<Long, BigDecimal> findForeignExpenseEntries(long participantId) {

        List<ExpenseEntry> expenseEntriesByForeigners = expenseEntryDao.findForeignDebtsInExpensesPaidByParticipant(participantId);

        return expenseEntriesByForeigners.stream()
            .collect(groupingBy(
                ExpenseEntry::getParticipantId,
                reducing(ZERO, ExpenseEntry::getAmount, BigDecimal::add)
            ));
    }

    private Map<Long, BigDecimal> findParticipantExpenseEntries(long participantId) {

        List<ExpenseEntry> debtsByParticipant = expenseEntryDao.findDebtsInForeignExpensesByParticipantId(participantId);

        return debtsByParticipant.stream()
            .collect(groupingBy(
                ExpenseEntry::getPayingParticipantId,
                reducing(ZERO, DebtInExpenseView::getAmount, BigDecimal::add)
            ));
    }

    public Map<Long, BigDecimal> showAllBalances(long groupId) {

        List<Participant> groupParticipants = participantDao.findByGroupId(groupId);
        Map<Long, BigDecimal> participantsBalances = new HashMap<>();

        groupParticipants.forEach(participant -> {
            BigDecimal balance = ZERO;
            for (BigDecimal debt : showScoreRelativeToForeignParticipants(participant.getId()).values()) {
                balance = balance.add(debt);
            }

            participantsBalances.put(participant.getId(), balance);
        });
        return participantsBalances;
    }

    public Map<Long, Map<Long, BigDecimal>> refactorDebts(long groupId) {

        List<Participant> groupParticipants = participantDao.findByGroupId(groupId);
        Map<Long, Map<Long, BigDecimal>> allParticipantToOtherParticipantsBalancesInGroup = new HashMap<>();

        groupParticipants.forEach(participant -> {
            allParticipantToOtherParticipantsBalancesInGroup.put(participant.getId(), showScoreRelativeToForeignParticipants(participant.getId()));
        });

        if (groupParticipants.size() > 2) {

            groupParticipants.forEach(participant -> {

                Map<Long, BigDecimal> participantDebts = allParticipantToOtherParticipantsBalancesInGroup.get(participant.getId());
                List<Long> positiveDebtId = new ArrayList<>();
                List<Long> negativeDebtId = new ArrayList<>();


                participantDebts.forEach((id, debt) -> {
                    if (debt.compareTo(ZERO) == 1) {
                        positiveDebtId.add(id);
                    } else
                        negativeDebtId.add(id);
                });

                negativeDebtId.forEach(negativeId -> {

                    Map<Long, BigDecimal> negIdParticipantDebts = allParticipantToOtherParticipantsBalancesInGroup.get(negativeId);

                    positiveDebtId.forEach(positiveId -> {

                        Map<Long, BigDecimal> posIdParticipantDebts = allParticipantToOtherParticipantsBalancesInGroup.get(positiveId);
                        BigDecimal negativeDebt = participantDebts.get(negativeId);
                        BigDecimal positiveDebt = participantDebts.get(positiveId);

                        if (positiveDebt.add(negativeDebt).compareTo(ZERO) == 1) {

                            participantDebts.put(negativeId, ZERO);
                            participantDebts.put(positiveId, positiveDebt.add(negativeDebt));

                            negIdParticipantDebts.put(participant.getId(), ZERO);
                            negIdParticipantDebts.put(positiveId, negIdParticipantDebts.get(positiveId).subtract(negativeDebt));

                            posIdParticipantDebts.put(participant.getId(), posIdParticipantDebts.get(participant.getId()).subtract(negativeDebt));
                            posIdParticipantDebts.put(negativeId, posIdParticipantDebts.get(negativeId).add(negativeDebt));
                        } else {

                            participantDebts.put(negativeId, negativeDebt.add(positiveDebt));
                            participantDebts.put(positiveId, ZERO);

                            negIdParticipantDebts.put(participant.getId(), ZERO.subtract(negativeDebt).subtract(positiveDebt));
                            negIdParticipantDebts.put(positiveId, negIdParticipantDebts.get(positiveId).add(positiveDebt));

                            posIdParticipantDebts.put(participant.getId(), ZERO);
                            posIdParticipantDebts.put(negativeId, posIdParticipantDebts.get(negativeId).subtract(positiveDebt));
                        }

                        allParticipantToOtherParticipantsBalancesInGroup.put(participant.getId(), participantDebts);
                        allParticipantToOtherParticipantsBalancesInGroup.put(negativeId, negIdParticipantDebts);
                        allParticipantToOtherParticipantsBalancesInGroup.put(positiveId, posIdParticipantDebts);
                    });
                });
            });
        }
        return allParticipantToOtherParticipantsBalancesInGroup;
    }
}
