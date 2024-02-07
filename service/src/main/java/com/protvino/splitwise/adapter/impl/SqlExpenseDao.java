package com.protvino.splitwise.adapter.impl;

import com.protvino.splitwise.adapter.ExpenseDao;
import com.protvino.splitwise.domain.request.EditExpenseRequest;
import com.protvino.splitwise.domain.value.Expense;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SqlExpenseDao implements ExpenseDao {

    private  final NamedParameterJdbcOperations jdbc;

    private final RowMapper<Expense> rowMapper = new ExpenseRowMapper();

    @Override
    public long create(EditExpenseRequest editExpenseRequest) {

        Timestamp now = Timestamp.from(Instant.now());

        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("paying_participant_id", editExpenseRequest.getPayingParticipantId())
            .addValue("total", editExpenseRequest.getTotal())
            .addValue("comment", editExpenseRequest.getComment())
            .addValue("created", now)
            .addValue("updated", now);

        String sql = """
            INSERT INTO expenses(paying_participant_id, total, comment, created, updated)
            VALUES (:paying_participant_id, :total, :comment, :created, : updated)
            RETURNING id
            """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update(sql,params,keyHolder);
        return keyHolder.getKeyAs(Long.class);
    }

    @Override
    public void update(long id, EditExpenseRequest editExpenseRequest) {

        Timestamp now = Timestamp.from(Instant.now());

        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("id", id)
            .addValue("paying_participant_id", editExpenseRequest.getPayingParticipantId())
            .addValue("total", editExpenseRequest.getTotal())
            .addValue("comment", editExpenseRequest.getComment())
            .addValue("updated", now);

        String sql = """
            UPDATE expenses
            SET paying_participant_id = :paying_participant_id,
                total = :total,
                comment = :comment,
                updated = :updated
            WHERE id = :id
            """;

        jdbc.update(sql,params);
    }

    @Override
    public Expense findById(long id) {

        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("id", id);

        String sql = """
            SELECT FROM expenses
            WHERE id = :id
            """;

        List<Expense> result = jdbc.query(sql,params,rowMapper);
        return !result.isEmpty() ? result.get(0) : null;
    }

    static class ExpenseRowMapper implements RowMapper<Expense> {
        @Override
        public Expense mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Expense(
                rs.getLong("id"),
                rs.getLong("paying_participant_id"),
                rs.getDouble("total"),
                rs.getString("comment")
            );
        }
    }
}
