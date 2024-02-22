package com.protvino.splitwise._inmem;

import com.protvino.splitwise.adapter.UserDao;
import com.protvino.splitwise.domain.request.EditUserRequest;
import com.protvino.splitwise.domain.value.User;
import lombok.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Value
public class InMemoryUserDao implements UserDao {

    Map<Long, User> rows = new HashMap<>();

    @Override
    public long create(EditUserRequest request) {

        long id = ThreadLocalRandom.current().nextLong(0, 100000);

        rows.put(id,new User(id, request.getUserName(), request.getPassword()));
        return id;
    }

    @Override
    public void update(long id, EditUserRequest request) {

        if(rows.containsKey(id)) {
            rows.put(id,new User(id, request.getUserName(), request.getPassword()));
        }
    }

    @Override
    public User findByUserName(String userName) {
        return rows.values()
                .stream()
                .filter(user -> user.getUserName().equals(userName))
                .findFirst().orElse(null);
    }

    @Override
    public boolean checkIfExists(String userName) {
        return rows.values()
                .stream()
                .anyMatch(it -> it.getUserName() == userName);
    }

    public void clear() {
        rows.clear();
    }
}
