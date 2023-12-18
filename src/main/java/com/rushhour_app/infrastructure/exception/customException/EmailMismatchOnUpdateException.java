package com.rushhour_app.infrastructure.exception.customException;

public class EmailMismatchOnUpdateException extends RuntimeException {

    public EmailMismatchOnUpdateException(String message) {
        super(message);
    }
}
