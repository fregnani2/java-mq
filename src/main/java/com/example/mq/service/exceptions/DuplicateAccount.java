package com.example.mq.service.exceptions;

/**
 * Exception thrown when trying to create an account that already exists
 */
public class DuplicateAccount extends RuntimeException{
    public DuplicateAccount(String message) {
        super(message);
    }
}
