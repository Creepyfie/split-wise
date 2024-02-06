package com.protvino.splitwise.domain._data;

import com.protvino.splitwise.domain.request.EditGroupRequest;
import org.instancio.Instancio;

public class EditGroupRequests {

    public static EditGroupRequest aGroup() {
        return Instancio.create(EditGroupRequest.class);
    }
}
