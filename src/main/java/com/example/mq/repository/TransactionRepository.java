package com.example.mq.repository;

import com.example.mq.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for Transaction entity
 */
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    /**
     * Custom query to get all transactions by account number
     * @param fromAccountNumber the account number from which the transactions were made
     */
    List<Transaction> findAllByFromAccountNumber(Integer fromAccountNumber);

    /**
     * Custom query to get all transactions by account number and status
     * @param accountNumber the account number from which the transactions were made
     * @param status the status of the transaction
     */
    List<Transaction> findAllByFromAccountNumberAndStatus (Integer accountNumber, String status);



}

