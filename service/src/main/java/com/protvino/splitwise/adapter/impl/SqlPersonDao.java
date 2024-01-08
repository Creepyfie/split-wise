package com.protvino.splitwise.adapter.impl;

import com.protvino.splitwise.adapter.PersonDao;
import com.protvino.splitwise.domain.request.EditPersonRequest;
import com.protvino.splitwise.domain.value.Person;
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
public class SqlPersonDao implements PersonDao {

    private final NamedParameterJdbcOperations jdbc;
    private final RowMapper<Person> personRowMapper = new PersonRowMapper();

    @Override
    public Long create(EditPersonRequest request) {

        Timestamp now = Timestamp.from(Instant.now());
        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("firstName", request.getFirstName())
            .addValue("lastName", request.getLastName())
            .addValue("created", now)
            .addValue("updated", now);

        String sql = """
            INSERT INTO persons (first_name, last_name, created, updated)
            VALUES (:firstName, :lastName, :created, :updated)
            RETURNING id""";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update(sql, params, keyHolder);
        return keyHolder.getKeyAs(Long.class);
    }

    @Override
    public void update(Long id, EditPersonRequest request) {
        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("id", id)
            .addValue("firstName", request.getFirstName())
            .addValue("lastName", request.getLastName())
            .addValue("updated", Timestamp.from(Instant.now()));

        String sql = """
            UPDATE persons
            SET first_name = :firstName, last_name = :lastName, updated = :updated
            WHERE id = :id""";

        jdbc.update(sql, params);
    }

    @Override
    public void delete(Long id) {
        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("id", id);

        String sql = """
            DELETE FROM persons
            WHERE id = :id""";

        jdbc.update(sql, params);
    }

    @Override
    public Person findById(Long id) {
        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("id", id);

        String sql = """
            SELECT * FROM persons
            WHERE id = :id""";

        List<Person> results = jdbc.query(sql, params, personRowMapper);
        return !results.isEmpty() ? results.get(0) : null;
    }

    static class PersonRowMapper implements RowMapper<Person> {
        @Override
        public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Person(
                rs.getLong("id"),
                rs.getString("first_name"),
                rs.getString("last_name")
            );
        }
    }
}
