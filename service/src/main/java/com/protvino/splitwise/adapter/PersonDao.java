package com.protvino.splitwise.adapter;

import com.protvino.splitwise.domain.request.EditPersonRequest;
import com.protvino.splitwise.domain.value.Person;

public interface PersonDao {

    long create(EditPersonRequest request);

    void update(Long id, EditPersonRequest request);

    void delete(Long id);

    Person findById(Long id);
}
