package com.protvino.splitwise.adapter.impl;

import com.protvino.splitwise.adapter.ExpenseEntryViewDao;
import com.protvino.splitwise.model.dto.ExpenseEntryView;
import com.protvino.splitwise.model.request.ExpenseEntrySearchRequest;
import com.protvino.splitwise.util.SqlFilters;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * DAO для модели чтения долей (expense_entry) участников в трате (expense).
 */
@Repository
@RequiredArgsConstructor
public class SqlExpenseEntryViewDao implements ExpenseEntryViewDao {

    private final NamedParameterJdbcOperations jdbc;
    private final RowMapper<ExpenseEntryView> rowMapper = new ExpenseEntryViewRowMapper();

    @Override
    public List<ExpenseEntryView> search(ExpenseEntrySearchRequest request) {

        SqlFilters filters = SqlFilters.builder()
            .in("expense_id", request.getExpenseIds())
            .in("participant_id", request.getDebtorParticipantIds())
            .build();

        String sql = """
            SELECT e.payer_participant_id, ee.debtor_participant_id, ee.amount
            FROM expense_entries ee
            JOIN expenses e ON e.id = ee.expense_id
            """ + filters.makeWhereClause();

        return jdbc.query(sql, filters.getParams(), rowMapper);
    }

    @Override
    public List<ExpenseEntryView> findDebtorEntriesInForeignExpenses(Long debtorParticipantId) {

        String sql = """
            SELECT e.payer_participant_id, debtor_participant_id, amount
            FROM expense_entries ee
            JOIN expenses e on ee.expense_id = e.id
            WHERE e.payer_participant_id <> :debtorParticipantId
              AND de.debtor_participant_id = :debtorParticipantId""";

        return jdbc.query(sql, Map.of("debtorParticipantId", debtorParticipantId), rowMapper);
    }

    @Override
    public List<ExpenseEntryView> findForeignEntriesInPayerExpenses(Long payerParticipantId) {

        String sql = """
            SELECT e.payer_participant_id, debtor_participant_id, amount
            FROM debt_in_expense de
            JOIN expenses e on de.expense_id = e.id
            WHERE e.payer_participant_id = :payerParticipantId
              AND de.debtor_participant_id <> :payerParticipantId""";

        return jdbc.query(sql, Map.of("payerParticipantId", payerParticipantId), rowMapper);
    }

    static class ExpenseEntryViewRowMapper implements RowMapper<ExpenseEntryView> {
        @Override
        public ExpenseEntryView mapRow(ResultSet rs, int rowNum) throws SQLException {
            return ExpenseEntryView.builder()
                .payerParticipantId(rs.getLong("payer_participant_id"))
                .debtorParticipantId(rs.getLong("debtor_participant_id"))
                .amount(rs.getBigDecimal("amount"))
                .build();
        }
    }
}
