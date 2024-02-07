package com.protvino.splitwise.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ParticipantAlreadyExistsException extends RuntimeException{
    public ParticipantAlreadyExistsException(String message) {
        super(message);
    }
}
