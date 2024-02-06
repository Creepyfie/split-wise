package com.protvino.splitwise.domain.request;

import lombok.Value;

@Value
public class EditParticipantRequest {

    Long personId;
    Long groupId;
}
