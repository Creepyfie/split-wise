package com.protvino.splitwise.port.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ParticipantDto {

    Long id;
    Long personId;
    Long groupId;
}
