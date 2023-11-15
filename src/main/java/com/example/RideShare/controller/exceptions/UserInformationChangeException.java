package com.example.RideShare.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UserInformationChangeException extends  RuntimeException{
    public UserInformationChangeException(String email) {
        super("Failed to make changes for user with email, " + email + ".");
    }
}
