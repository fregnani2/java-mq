package com.example.mq.service;

import com.example.mq.model.Client;
import com.example.mq.model.ClientRole;
import com.example.mq.model.Transaction;
import com.example.mq.repository.ClientRepository;
import com.example.mq.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class WorkerServiceTest {

    @Autowired
    @InjectMocks
    private WorkerService workerService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ClientRepository clientRepository;


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should make a transfer between two accounts")
    void makeTransfer() throws Exception {
        Client fromAccount = new Client(1,"Bruno", 324,1000.0, ClientRole.CLIENT,"password123", "bruno@gmail.com");
        Client toAccount = new Client(2,"Pedro", 222,1000.0, ClientRole.CLIENT,"password123", "abc@gmail.com");

        when(clientRepository.getClientByAccountNumber(324)).thenReturn(fromAccount);
        when(clientRepository.getClientByAccountNumber(222)).thenReturn(toAccount);

        Transaction transaction = new Transaction(1, 222, 324, 100.0, LocalDateTime.now(),"PROCESSING");

        workerService.processTransaction(transaction);


        verify(clientRepository, times(1)).getClientByAccountNumber(324);
        verify(clientRepository, times(1)).getClientByAccountNumber(222);
        verify(transactionRepository, times(1)).save(any(Transaction.class));

        assert fromAccount.getBalance() == 900.0;
        assert toAccount.getBalance() == 1100.0;
        assert transaction.getStatus().equals("COMPLETED");
    }



}
