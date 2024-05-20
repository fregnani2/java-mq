package com.example.mq.service;

import com.example.mq.model.Client;
import com.example.mq.model.Transaction;
import com.example.mq.repository.ClientRepository;
import com.example.mq.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

/**
 * Service for worker operations
 * Annotations:
 * - @Service: Indicates that an annotated class is a "Service"
 * - @Autowired: Marks a constructor, field, setter method, or config method as to be autowired by Spring's dependency injection facilities.
 * - @JmsListener: Listener to a queue.
 */
@Service
public class WorkerService {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Process a transaction
     * @param transaction Transaction object to process
     */
    @JmsListener(destination = "DEV.QUEUE.1")
    public void processTransaction(Transaction transaction) {
        try{
            Thread.sleep(5000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        Client sender = clientRepository.getClientByAccountNumber(transaction.getFromAccountNumber());
        Client receiver = clientRepository.getClientByAccountNumber(transaction.getToAccountNumber());

        sender.setBalance(sender.getBalance() - transaction.getAmount());
        receiver.setBalance(receiver.getBalance() + transaction.getAmount());

        clientRepository.save(sender);
        clientRepository.save(receiver);

        transaction.setStatus("COMPLETED");
        transactionRepository.save(transaction);
    }
}
