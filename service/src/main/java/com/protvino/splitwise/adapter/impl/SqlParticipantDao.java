package com.protvino.splitwise.adapter.impl;

import com.protvino.splitwise.adapter.ParticipantDao;
import com.protvino.splitwise.domain.request.EditParticipantRequest;
import com.protvino.splitwise.domain.value.Participant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
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
public class SqlParticipantDao implements ParticipantDao {

    private final NamedParameterJdbcOperations jdbc;
    private final RowMapper<Participant> rowMapper = new ParticipantRowMapper();

    @Override
    public long create(EditParticipantRequest editParticipantRequest) {

        Timestamp now = Timestamp.from(Instant.now());

        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("person_id", editParticipantRequest.getPersonId())
            .addValue("group_id", editParticipantRequest.getGroupId())
            .addValue("created", now)
            .addValue("updated", now);

        String sql = """
            INSERT INTO participants(person_id, group_id, created, updated)
            VALUES (:person_id,:group_id,:created,:updated)
            RETURNING id""";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update(sql, params, keyHolder);

        return keyHolder.getKeyAs(Long.class);
    }

    @Override
    public void update(long id, EditParticipantRequest editParticipantRequest) {

        Timestamp now = Timestamp.from(Instant.now());

        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("id", id)
            .addValue("person_id", editParticipantRequest.getPersonId())
            .addValue("group_id", editParticipantRequest.getGroupId())
            .addValue("updated", now);

        String sql = """
            UPDATE participants
            SET person_id = :person_id, group_id = :group_id, updated = :updated
            WHERE id = :id""";

        jdbc.update(sql, params);
    }

    @Override
    public void delete(EditParticipantRequest editParticipantRequest) {

        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("group_id", editParticipantRequest.getGroupId());

        String sql = """
            DELETE FROM participants
            WHERE group_id = :group_id""";

        jdbc.update(sql, params);
    }

    @Override
    public void delete(long id) {

        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("id", id);

        String sql = """
            DELETE FROM participants
            WHERE id = :id""";

        jdbc.update(sql, params);
    }

    @Override
    public Participant findById(Long id) {

        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("id", id);

        String sql = """
            SELECT FROM participants
            WHERE id = :id
            """;
        List<Participant> result = jdbc.query(sql, params, rowMapper);

        return !result.isEmpty() ? result.get(0) : null;
    }

    @Override
    public boolean checkIfExists(long personId, long groupId) {

        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("personId", personId)
            .addValue("groupId", groupId);

        String sql = """
            SELECT EXISTS FROM (
              SELECT 1 FROM participants
              WHERE person_id = :personId AND group_id = :groupId)""";
        return !jdbc.query(sql, params, (rs, i) -> rs.getBoolean(1)).get(0);
    }

    static class ParticipantRowMapper implements RowMapper<Participant> {
        @Override
        public Participant mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Participant(
                rs.getLong("id"),
                rs.getLong("person_id"),
                rs.getLong("group_id")
            );
        }
    }
}
