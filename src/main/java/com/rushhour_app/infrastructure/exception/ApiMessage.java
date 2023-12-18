package com.rushhour_app.infrastructure.exception;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class ApiMessage {

    private final String message;
    private final HttpStatus status;
    private final ZonedDateTime timestamp;

    public ApiMessage(String message, HttpStatus status, ZonedDateTime timestamp) {
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }
}
