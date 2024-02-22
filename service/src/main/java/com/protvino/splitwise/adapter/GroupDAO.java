package com.protvino.splitwise.adapter;

import com.protvino.splitwise.domain.request.EditGroupRequest;
import com.protvino.splitwise.domain.value.Group;

public interface GroupDAO {

    long create(EditGroupRequest editGroupRequest);

    void update(Long id, EditGroupRequest editGroupRequest);

    void delete(Long id);

    Group findById(Long id);
}
