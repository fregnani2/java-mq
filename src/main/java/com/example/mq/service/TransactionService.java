package com.example.mq.service;

import com.example.mq.model.Client;
import com.example.mq.model.Transaction;
import com.example.mq.repository.ClientRepository;
import com.example.mq.repository.TransactionRepository;
import com.example.mq.service.exceptions.EntityNotFound;
import com.example.mq.service.exceptions.TransferAmount;
import com.example.mq.service.exceptions.WrongArgument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for transaction operations
 * Annotations:
 * - @Service: Indicates that an annotated class is a "Service"
 * - @Autowired: Marks a constructor, field, setter method, or config method as to be autowired by Spring's dependency injection facilities.
 */
@Service
public class TransactionService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ClientService clientService;
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ClientRepository clientRepository;

    /**
     * Method to get all transactions by account number
     * @param accountNumber the account number from which the transactions were made
     */

    public List<Transaction> getTransactionByAccountNumber(Integer accountNumber) {
        logger.info("Getting transactions of account number" + accountNumber);
        return transactionRepository.findAllByFromAccountNumber(accountNumber);
    }

    /**
     * Send a transaction to the queue, and save it to the database with status PROCESSING
     * @param obj Transaction object to send
     */
    public void enqueueTransaction(Transaction obj)  {
        logger.info("Enqueuing transaction from account number {} to account number {}", obj.getFromAccountNumber(), obj.getToAccountNumber());
        if(clientRepository.getClientByAccountNumber(obj.getToAccountNumber()) == null){
            throw new EntityNotFound("Receiver account number does not exist");
        }
        Client sender = clientService.getClientByAccountNumber(obj.getFromAccountNumber());

        if (obj.getAmount() <= 0) {
            throw new TransferAmount("Minimum amount to transfer is 1");
        }
        if(sender.getBalance() < obj.getAmount()){
            throw new TransferAmount("Insufficient balance");
        }
        if(isTransactionInQueue(obj.getFromAccountNumber())){
            throw new WrongArgument("Wait for the previous transaction to finish processing");
        }
        obj.setDate(LocalDateTime.now());
        obj.setStatus("PROCESSING");

        transactionRepository.save(obj);
        logger.info("Transaction saved to database");

        jmsTemplate.convertAndSend("DEV.QUEUE.1", obj);
        logger.info("Transaction sent to queue");
    }

    /**
     * Check if a transaction from the sender account number is already in the queue
     * @param accountNumber Account number to check
     * @return true if a transaction from the same account number is in the queue, false otherwise
     */
    public boolean isTransactionInQueue(Integer accountNumber) {
        List<Transaction> transactions = transactionRepository.findAllByFromAccountNumberAndStatus(accountNumber, "PROCESSING");
        return !transactions.isEmpty();
    }


}
