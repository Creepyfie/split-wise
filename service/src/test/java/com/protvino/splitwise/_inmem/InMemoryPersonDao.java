package com.protvino.splitwise._inmem;

import com.protvino.splitwise.adapter.PersonDao;
import com.protvino.splitwise.domain.request.EditPersonRequest;
import com.protvino.splitwise.domain.value.Person;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class InMemoryPersonDao implements PersonDao {

    Map<Long, Person> rows = new HashMap<>();

    @Override
    public long create(EditPersonRequest request) {

        long id = ThreadLocalRandom.current().nextLong(0, 100000);

        rows.put(id, new Person(id, request.getFirstName(), request.getLastName()));
        return id;
    }

    @Override
    public void update(Long id, EditPersonRequest request) {

        if (rows.containsKey(id)) {
            Person updatePerson = new Person(id, request.getFirstName(), request.getLastName());
            rows.put(id, updatePerson);
        }
    }

    @Override
    public void delete(Long id) {
        rows.remove(id);
    }

    @Override
    public Person findById(Long id) {
        return rows.get(id);
    }

    public void clear() {
        rows.clear();
    }
}
