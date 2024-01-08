package com.protvino.splitwise.port.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PersonDto {

    Integer id;
    String firstName;
    String lastName;
}
