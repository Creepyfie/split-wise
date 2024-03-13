package com.protvino.splitwise.port.dto;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@Builder
@RequiredArgsConstructor
public class GroupDto {

    Long id;
    String name;
}
