package com.rushhour_app.infrastructure.exception.customException;

public class ForbidenException extends RuntimeException {
    public ForbidenException(String message) {
        super(message);
    }
}
