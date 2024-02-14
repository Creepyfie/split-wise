package com.protvino.splitwise.adapter.impl;

import com.protvino.splitwise.adapter.UserDao;
import com.protvino.splitwise.domain.request.EditUserRequest;
import com.protvino.splitwise.domain.value.User;
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
public class SqlUserDao implements UserDao {

    private final NamedParameterJdbcOperations jdbc;
    private final RowMapper<User> rowMapper = new UserRowMapper();



    @Override
    public long create(EditUserRequest request) {

        Timestamp now = Timestamp.from(Instant.now());

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("userName", request.getUserName())
                .addValue("password", request.getPassword())
                .addValue("created", now)
                .addValue("updated", now);

        String sql = """
                INSERT INTO Users (user_name, password, created, updated)
                VALUES (:userName, :password, :created, :updated)
                RETURNING id
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        long id = jdbc.update(sql,params,keyHolder);


        return keyHolder.getKeyAs(Long.class);
    }

    @Override
    public void update(long id, EditUserRequest request) {

        Timestamp now = Timestamp.from(Instant.now());

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("userName", request.getUserName())
                .addValue("password", request.getPassword())
                .addValue("updated", now);

        String sql = """
                UPDATE Users 
                SET user_name = :userName, password :password, updated= updated
                WHERE id = :id
                """;

        jdbc.update(sql, params);
    }

    @Override
    public User findByUserName(String userName) {

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("userName", userName);

        String sql = """
                SELECT FROM Users 
                WHERE user_name = :userName
                """;

        List<User> users = jdbc.query(sql,params, rowMapper);
        return !users.isEmpty() ? users.get(0) : null ;
    }

    static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(
                    rs.getLong("id"),
                    rs.getString("user_name"),
                    rs.getString("password")
            );
        }
    }
}
