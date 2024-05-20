package com.example.mq.controller;


import com.example.mq.model.Transaction;
import com.example.mq.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for transaction operations
 * Annotations:
 * - @RestController: Indicates that an annotated class is a "Controller"
 * - @RequestMapping: Maps web requests to Spring Controller methods
 * - @PostMapping: Maps HTTP POST requests onto specific handler methods
 * - @GetMapping: Maps HTTP GET requests onto specific handler methods
 */
@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    /**
     * Make a new transaction
     * @param transaction transaction object
     */
    @PostMapping()
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        transactionService.enqueueTransaction(transaction);
        return ResponseEntity.ok().body(transaction);
    }

    /**
     * Get all transactions by account number
     * @param accountNumber account number
     */
    @GetMapping("/{accountNumber}")
    public ResponseEntity<List<Transaction>> getTransactionByAccountNumber(@PathVariable Integer accountNumber) {
        return ResponseEntity.ok().body(transactionService.getTransactionByAccountNumber(accountNumber));
    }

}
