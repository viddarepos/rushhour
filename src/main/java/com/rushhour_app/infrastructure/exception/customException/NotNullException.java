package com.rushhour_app.infrastructure.exception.customException;

public class NotNullException extends RuntimeException {

    public NotNullException(String message) {
        super(message);
    }
}
