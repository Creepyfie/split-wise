package com.protvino.splitwise.adapter.impl;

import com.protvino.splitwise.adapter.DebtInExpenseDao;
import com.protvino.splitwise.domain.request.EditDebtInExpenseRequest;
import com.protvino.splitwise.domain.value.DebtInExpense;
import com.protvino.splitwise.model.DebtInExpenseView;
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
public class SqlDebtInExpenseDao implements DebtInExpenseDao {

    private final NamedParameterJdbcOperations jdbc;

    private final RowMapper<DebtInExpense> rowMapper = new DebtInExpenseRowMapper();
    private final RowMapper<DebtInExpenseView> viewRowMapper = new DebtInExpenseViewRowMapper();

    @Override
    public void create(EditDebtInExpenseRequest request) {

        Timestamp now = Timestamp.from(Instant.now());

        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("expense_id", request.getExpenseId())
            .addValue("from", request.getFromParticipantId())
            .addValue("to", request.getToParticipantId())
            .addValue("amount", request.getAmount())
            .addValue("created", now)
            .addValue("updated", now);

        String sql = """
            INSERT INTO debt_in_expense(expense_id, from_participant_id, to_participant_id, amount, created, updated)
            VALUES (:expense_id, :from, :to, :amount, :created,:updated)""";

        jdbc.update(sql, params);
    }

    @Override
    public void update(EditDebtInExpenseRequest request) {

        Timestamp now = Timestamp.from(Instant.now());

        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("expense_id", request.getExpenseId())
            .addValue("from", request.getFromParticipantId())
            .addValue("to", request.getToParticipantId())
            .addValue("amount", request.getAmount())
            .addValue("updated", now);

        String sql = """
            UPDATE debt_in_expense
            SET amount = :amount,
                updated = :updated
            WHERE expense_id = :expense_id AND from_participant_id = :from AND to_participant_id = :to""";

        jdbc.update(sql, params);
    }

    @Override
    public void update(List<EditDebtInExpenseRequest> requests) {

        Timestamp now = Timestamp.from(Instant.now());
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(EditDebtInExpenseRequest request) {

        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("expense_id", request.getExpenseId())
            .addValue("from", request.getFromParticipantId())
            .addValue("to", request.getToParticipantId());

        String sql = """
            DELETE FROM debt_in_expense
            WHERE expense_id = :expense_id AND from_participant_id = :from AND to_participant_id = :to""";

        jdbc.update(sql, params);
    }

    @Override
    public List<DebtInExpense> findByExpenseId(Long expenseId) {

        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("expense_id", expenseId);

        String sql = """
            SELECT FROM debt_in_expense
            WHERE expense_id = :expense_id""";

        List<DebtInExpense> debts = jdbc.query(sql, params, rowMapper);

        return debts;
    }

    @Override
    public List<DebtInExpense> findByFromId(Long fromId) {

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("fromId", fromId);

        String sql = """
            SELECT de.* FROM debt_in_expense de
            WHERE from_participant_id = :fromId""";

        List<DebtInExpense> debts = jdbc.query(sql, params, rowMapper);

        return debts;
    }

    @Override
    public List<DebtInExpenseView> findDebtsInForeignExpensesByParticipantId(Long participantId) {

        String sql = """
            SELECT e.paying_participant_id, participant_id, amount
            FROM debt_in_expense de
            JOIN expenses e on de.expense_id = e.id
            WHERE e.paying_participant_id <> :participantId
              AND de.from_participant_id = :participantId""";

        return jdbc.query(sql, Map.of("participantId", participantId), viewRowMapper);
    }

    @Override
    public List<DebtInExpenseView> findForeignDebtsInExpensesPaidByParticipant(Long participantId) {

        String sql = """
            SELECT e.paying_participant_id, participant_id, amount
            FROM debt_in_expense de
            JOIN expenses e on de.expense_id = e.id
            WHERE e.paying_participant_id = :participantId
              AND de.from_participant_id <> :participantId""";

        return jdbc.query(sql, Map.of("participantId", participantId), viewRowMapper);
    }

    static class DebtInExpenseRowMapper implements RowMapper<DebtInExpense> {
        @Override
        public DebtInExpense mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new DebtInExpense(
                rs.getLong("expense_id"),
                rs.getLong("from_participant_id"),
                rs.getLong("to_participant_id"),
                rs.getBigDecimal("amount")
            );
        }
    }

    static class DebtInExpenseViewRowMapper implements RowMapper<DebtInExpenseView> {
        @Override
        public DebtInExpenseView mapRow(ResultSet rs, int rowNum) throws SQLException {
            return  DebtInExpenseView.builder()
                .payingParticipantId(rs.getLong("paying_participant_id"))
                .participantId(rs.getLong("participant_id"))
                .amount(rs.getBigDecimal("amount"))
                .build();
        }
    }
}
