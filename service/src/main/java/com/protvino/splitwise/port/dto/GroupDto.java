package com.protvino.splitwise.port.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GroupDto {
    Long id;
    String name;
}
