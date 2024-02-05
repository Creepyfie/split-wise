package com.protvino.splitwise.port.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ParticipantDto {

    Long id;
    Long person_id;
    Long group_id;
}
