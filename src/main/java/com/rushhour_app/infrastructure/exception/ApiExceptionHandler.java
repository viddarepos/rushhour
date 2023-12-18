package com.rushhour_app.infrastructure.exception;

import com.rushhour_app.infrastructure.exception.customException.ConflictException;
import com.rushhour_app.infrastructure.exception.customException.EmailMismatchOnUpdateException;
import com.rushhour_app.infrastructure.exception.customException.InternalServerError;
import com.rushhour_app.infrastructure.exception.customException.NotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {SQLIntegrityConstraintViolationException.class})
    public ResponseEntity<Object> handleSQLIntegrityConstraintViolationException(Exception ex) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiMessage api = new ApiMessage(ex.getMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now());

        return new ResponseEntity<>(api, badRequest);
    }

    @ExceptionHandler(value = {EmptyResultDataAccessException.class})
    public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiMessage api = new ApiMessage(ex.getMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now());

        return new ResponseEntity<>(api, badRequest);
    }

    @ExceptionHandler(value = {EmailMismatchOnUpdateException.class})
    public ResponseEntity<Object> handleEmailMismatchOnUpdateException(EmailMismatchOnUpdateException ex) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiMessage api = new ApiMessage(ex.getMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now());

        return new ResponseEntity<>(api, badRequest);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMethodArgumentException(MethodArgumentNotValidException ex) {
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        return errorMap;
    }

    @ExceptionHandler(value = {ConflictException.class})
    public ResponseEntity<Object> handleConflictException(ConflictException ex) {
        HttpStatus badRequest = HttpStatus.CONFLICT;
        ApiMessage api = new ApiMessage(ex.getMessage(), HttpStatus.CONFLICT, ZonedDateTime.now());

        return new ResponseEntity<>(api, badRequest);
    }

    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        HttpStatus badRequest = HttpStatus.CONFLICT;
        ApiMessage api = new ApiMessage(ex.getMessage(), HttpStatus.CONFLICT, ZonedDateTime.now());

        return new ResponseEntity<>(api, badRequest);
    }

    @ExceptionHandler(value = {InternalServerError.class})
    public ResponseEntity<Object> handleInternalServerError(InternalServerError ex) {
        ApiMessage api = new ApiMessage(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ZonedDateTime.now());
        return new ResponseEntity<>(api, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex) {
        ApiMessage api = new ApiMessage(ex.getMessage(), HttpStatus.NOT_FOUND, ZonedDateTime.now());
        return new ResponseEntity<>(api, HttpStatus.NOT_FOUND);
    }

}
