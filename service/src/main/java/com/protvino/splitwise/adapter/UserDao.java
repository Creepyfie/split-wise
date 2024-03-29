package com.protvino.splitwise.adapter;

import com.protvino.splitwise.domain.request.EditUserRequest;
import com.protvino.splitwise.domain.value.User;

public interface UserDao {

    long create(EditUserRequest request);

    void update(long id, EditUserRequest request);

    User findByUserName(String userName);

    boolean checkIfExists(String userName);
}
