package com.protvino.splitwise.port.dto;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@Builder
@RequiredArgsConstructor
public class PersonDto {

    Long id;
    String firstName;
    String lastName;
}
