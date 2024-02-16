package com.kaloyan.notetakingapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class DifferentUserException extends RuntimeException{
    public DifferentUserException(String message){
        super(message);
    }
}
