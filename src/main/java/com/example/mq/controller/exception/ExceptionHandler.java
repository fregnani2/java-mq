package com.example.mq.controller.exception;


import com.example.mq.service.exceptions.DuplicateAccount;
import com.example.mq.service.exceptions.EntityNotFound;
import com.example.mq.service.exceptions.TransferAmount;
import com.example.mq.service.exceptions.WrongArgument;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

/**
 * class responsible for handling exceptions.
 * Tag @RestControllerAdvice centralize exception handling for @RequestMapping methods
 */
@RestControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(EntityNotFound.class)
    public ResponseEntity<Object> handleEntityNotFound(EntityNotFound ex, HttpServletRequest request) {
        String error = "Not found";
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponse response = new ErrorResponse(Instant.now(), status.value(), error, ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(response);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(DuplicateAccount.class)
    public ResponseEntity<Object> handleDuplicateAccount(DuplicateAccount ex, HttpServletRequest request) {
        String error = "Conflict";
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorResponse response = new ErrorResponse(Instant.now(), status.value(), error, ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(response);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({TransferAmount.class, WrongArgument.class})
    public ResponseEntity<Object> handleInsufficientBalance(Exception ex, HttpServletRequest request) {
        String error = "Bad Request";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse response = new ErrorResponse(Instant.now(), status.value(), error, ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(response);
    }
}
