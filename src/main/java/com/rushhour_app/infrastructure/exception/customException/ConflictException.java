package com.rushhour_app.infrastructure.exception.customException;

public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
