package com.protvino.splitwise.domain.request;

import lombok.Value;

@Value
public class EditParticipantRequest {

    Long person_id;
    Long group_id;
}
