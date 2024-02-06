package com.protvino.splitwise._inmem;

import com.protvino.splitwise.adapter.GroupDao;
import com.protvino.splitwise.domain.request.EditGroupRequest;
import com.protvino.splitwise.domain.value.Group;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class InMemoryGroupDao  implements GroupDao {

    Map<Long, Group> rows = new HashMap<>();

    @Override
    public long create(EditGroupRequest editGroupRequest) {
        long id = ThreadLocalRandom.current().nextLong(0, 100000);
        rows.put(id, new Group(id, editGroupRequest.getName()));
        return id;
    }

    @Override
    public void update(Long id, EditGroupRequest editGroupRequest) {
        if (rows.containsKey(id)) {
            rows.put(id, new Group(id, editGroupRequest.getName()));
        }
    }

    @Override
    public void delete(Long id) {
        rows.remove(id);
    }

    @Override
    public Group findById(Long id) {
        return rows.get(id);
    }

    public void clear(){ rows.clear();
    }
}
