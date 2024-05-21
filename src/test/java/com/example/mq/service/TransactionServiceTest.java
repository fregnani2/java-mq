package com.example.mq.service;


import com.example.mq.model.Client;
import com.example.mq.model.ClientRole;
import com.example.mq.model.Transaction;
import com.example.mq.repository.ClientRepository;
import com.example.mq.repository.TransactionRepository;
import com.example.mq.service.exceptions.EntityNotFound;
import com.example.mq.service.exceptions.TransferAmount;
import com.example.mq.service.exceptions.WrongArgument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class TransactionServiceTest {
    @Autowired
    @InjectMocks
    private TransactionService transactionService;


    @Mock
    private ClientService clientService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ClientRepository clientRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should not enqueue a transaction with a negative amount")
    void enqueueTransaction() {
        Client fromAccount = new Client(1,"Bruno", 324,1000.0, ClientRole.CLIENT,"password123", "bruno@gmail.com");
        Client toAccount = new Client(2,"Pedro", 222,1000.0, ClientRole.CLIENT,"password123", "abc@gmail.com");

        when(clientRepository.getClientByAccountNumber(324)).thenReturn(fromAccount);
        when(clientRepository.getClientByAccountNumber(222)).thenReturn(toAccount);

        Transaction transaction = new Transaction(1, 324,222, -100.0, LocalDateTime.now(),"PROCESSING");

        assertThrows(TransferAmount.class, () -> transactionService.enqueueTransaction(transaction));
    }

    @Test
    @DisplayName("Should not enqueue a transaction with a zero amount")
    void enqueueTransactionWithZeroAmount() {
        Client fromAccount = new Client(1,"Bruno", 324,1000.0, ClientRole.CLIENT,"password123", "bruno@gmail.com");
        Client toAccount = new Client(2,"Pedro", 222,1000.0, ClientRole.CLIENT,"password123", "abc@gmail.com");

        when(clientRepository.getClientByAccountNumber(324)).thenReturn(fromAccount);
        when(clientRepository.getClientByAccountNumber(222)).thenReturn(toAccount);

        Transaction transaction = new Transaction(1, 324,222, 0.0, LocalDateTime.now(),"PROCESSING");

        assertThrows(TransferAmount.class, () -> transactionService.enqueueTransaction(transaction));
    }

    @Test
    @DisplayName("Should not enqueue a transaction to a non-existing account")
    void enqueueTransactionToNonExistingAccount() {
        Client fromAccount = new Client(1,"Bruno", 324,1000.0, ClientRole.CLIENT,"password123", "bruno@gmail.com");

        clientRepository.save(fromAccount);


        Transaction transaction = new Transaction(1, 111, 324, 100.0, LocalDateTime.now(), "PROCESSING");

        assertThrows(EntityNotFound.class, () -> transactionService.enqueueTransaction(transaction));
    }
    }