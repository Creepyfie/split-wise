package com.protvino.splitwise.adapter.impl;

import com.protvino.splitwise.adapter.GroupDao;
import com.protvino.splitwise.domain.request.EditGroupRequest;
import com.protvino.splitwise.domain.value.Group;
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
public class SqlGroupDao implements GroupDao {

    private final NamedParameterJdbcOperations jdbc;
    private final RowMapper<Group> groupRowMapper = new GroupRowMapper();

    @Override
    public long create(EditGroupRequest request) {
        Timestamp now = Timestamp.from(Instant.now());

        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("name", request.getName())
            .addValue("created", now)
            .addValue("updated", now);

        String sql = """
            INSERT INTO groups(name, created, updated)
            VALUES (:name, :created, :updated)
            RETURNING id""";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update(sql, params, keyHolder);
        return keyHolder.getKeyAs(Long.class);
    }

    @Override
    public void update(Long id, EditGroupRequest request) {
        Timestamp now = Timestamp.from(Instant.now());

        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("id", id)
            .addValue("name", request.getName())
            .addValue("updated", now);

        String sql = """
            UPDATE groups
            SET name = :name, updated = :updated
            WHERE id = :id""";

        jdbc.update(sql, params);
    }

    @Override
    public void delete(Long id) {

        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("id", id);

        String sql = """
            DELETE FROM groups
            Where id = :id""";

        jdbc.update(sql, params);
    }

    @Override
    public Group findById(Long id) {

        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("id", id);

        String sql = """
            SELECT FROM gorups
            WHERE id = :id""";

        List<Group> results = jdbc.query(sql, params, groupRowMapper);

        return !results.isEmpty() ? results.get(0) : null;
    }

    static class GroupRowMapper implements RowMapper<Group> {
        @Override
        public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Group(
                rs.getLong("id"),
                rs.getString("name")
            );
        }
    }
}
