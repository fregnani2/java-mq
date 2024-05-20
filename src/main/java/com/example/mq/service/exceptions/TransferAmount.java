package com.example.mq.service.exceptions;

/**
 * Exception thrown when trying to transfer an amount that is greater than the balance or <= 0
 */
public class TransferAmount extends RuntimeException {
    public TransferAmount(String message) {
        super(message);
    }
}
