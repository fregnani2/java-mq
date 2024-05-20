package com.example.mq.service.exceptions;

/**
 * Exception thrown when an entity is not found
 */
public class EntityNotFound extends  RuntimeException{
    public EntityNotFound(String message) {
        super(message);
    }
}
