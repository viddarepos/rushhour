package com.rushhour_app.infrastructure.exception.customException;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
