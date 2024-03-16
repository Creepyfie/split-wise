package com.protvino.splitwise.adapter.impl;

import com.protvino.splitwise.adapter.ExpenseEntryDao;
import com.protvino.splitwise.domain.request.EditDebtInExpenseRequest;
import com.protvino.splitwise.domain.value.ExpenseEntry;
import com.protvino.splitwise.model.DebtInExpenseView;
import com.protvino.splitwise.model.request.ExpenseEntrySearchRequest;
import com.protvino.splitwise.util.SqlFilters;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class SqlExpenseEntryInExpenseDao implements ExpenseEntryDao {

    private final NamedParameterJdbcOperations jdbc;

    private final RowMapper<ExpenseEntry> rowMapper = new DebtInExpenseRowMapper();
    private final RowMapper<ExpenseEntry> viewRowMapper = new DebtInExpenseViewRowMapper();

    @Override
    public void create(EditDebtInExpenseRequest request) {

        Timestamp now = Timestamp.from(Instant.now());

        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("expense_id", request.getExpenseId())
            .addValue("participantId", request.getParticipantId())
            .addValue("amount", request.getAmount())
            .addValue("created", now)
            .addValue("updated", now);

        String sql = """
            INSERT INTO debts_in_expense(expense_id, participant_id, amount, created, updated)
            VALUES (:expense_id, :participantId, :amount, :created,:updated)""";

        jdbc.update(sql, params);
    }

    @Override
    public void update(EditDebtInExpenseRequest request) {

        Timestamp now = Timestamp.from(Instant.now());

        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("expense_id", request.getExpenseId())
            .addValue("participantId", request.getParticipantId())
            .addValue("amount", request.getAmount())
            .addValue("updated", now);

        String sql = """
            UPDATE debts_in_expense
            SET amount = :amount,
                updated = :updated
            WHERE expense_id = :expense_id AND participant_id = :participantId""";

        jdbc.update(sql, params);
    }

    @Override
    public void updateBatch(List<EditDebtInExpenseRequest> requests) {

        Timestamp now = Timestamp.from(Instant.now());
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(ExpenseEntry.Id id) {

        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("expenseId", id.getExpenseId())
            .addValue("participantId", id.getParticipantId());

        String sql = """
            DELETE FROM debts_in_expense
            WHERE expense_id = :expenseId AND participant_id = :participantId""";

        jdbc.update(sql, params);
    }

    static class DebtInExpenseRowMapper implements RowMapper<ExpenseEntry> {
        @Override
        public ExpenseEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
            return ExpenseEntry.of(
                ExpenseEntry.Id.of(
                    rs.getLong("expense_id"),
                    rs.getLong("participant_id")
                ),
                rs.getBigDecimal("amount")
            );
        }
    }
}
