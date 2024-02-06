package com.protvino.splitwise.domain._data;

import com.protvino.splitwise.domain.request.EditPersonRequest;
import org.instancio.Instancio;

public class EditPersonRequests {

    public static EditPersonRequest aPerson() {
        return Instancio.create(EditPersonRequest.class);
    }
}
