package com.example.RideShare.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserNoLongerExistsOnAuthException extends RuntimeException {
    public UserNoLongerExistsOnAuthException(String email) {
        super(String.format("User with email, %s, no longer exists", email));
    }
}
