package com.eascapeco.cinemapr.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RefreshTokenNotFoundException extends RuntimeException {

    private final String uid;

    public RefreshTokenNotFoundException(String uid) {
        super(String.format("RefreshToken is not found: uid [%s]", uid));
        this.uid = uid;
    }
}
