package com.rushhour_app.infrastructure.exception.customException;

public class InternalServerError extends RuntimeException {

    public InternalServerError(String message) {
        super(message);
    }
}
