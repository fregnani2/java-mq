package com.example.mq.service;


import com.example.mq.model.Client;
import com.example.mq.model.ClientRole;
import com.example.mq.repository.ClientRepository;
import com.example.mq.service.exceptions.DuplicateAccount;
import com.example.mq.service.exceptions.EntityNotFound;
import com.example.mq.service.exceptions.WrongArgument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest()
public class ClientServiceTest {
    @Mock
    private ClientRepository clientRepository;

    @Autowired
    @InjectMocks
    private ClientService clientService;

     /** Setup before each test
     * Initialize @Mock objects before each test
     */
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should not create a client with an existing account number")
    void createClientWithExistingAccountNumber() {

        Client client1 = new Client(1,"Bruno", 324,1000.0, ClientRole.CLIENT,"password123", "bruno@gmail.com");
        Client client2 = new Client(2,"Pedro", 324,1000.0, ClientRole.CLIENT,"password123", "abc@gmail.com");

        when(clientRepository.getClientByAccountNumber(324)).thenReturn(client1);
        assertThrows(DuplicateAccount.class, () -> clientService.createClient(client2));
    }

    @Test
    @DisplayName("Should not create a client with an existing email")
    void createClientWithExistingEmail() {

        Client client1 = new Client(1, "Bruno", 324, 1000.0, ClientRole.CLIENT, "password123", "bruno@gmail.com");
        Client client2 = new Client(2, "Pedro", 325, 1000.0, ClientRole.CLIENT, "password123", "bruno@gmail.com");

        when(clientRepository.getClientByEmail("bruno@gmail.com")).thenReturn(client1);
        assertThrows(DuplicateAccount.class, () -> clientService.createClient(client2));
    }

    @Test
    @DisplayName("Should not create a client with a negative balance")
    void createClientWithNegativeBalance() {

        Client client1 = new Client(1, "Bruno", 324, -1000.0, ClientRole.CLIENT, "password123", "bruno@gmail.com");

        assertThrows(WrongArgument.class, () -> clientService.createClient(client1));
    }

    @Test
    @DisplayName("Should not create a client with a empty name")
    void createClientWithEmptyName() {

        Client client1 = new Client(1, "", 324, 1000.0, ClientRole.CLIENT, "password123", "bruno@gmail.com");
        assertThrows(WrongArgument.class, () -> clientService.createClient(client1));
    }

    @Test
    @DisplayName("Should create a client")
    void createClient(){
        Client client = new Client(1, "Bruno", 324, 1000.0, ClientRole.CLIENT, "password123", "bruno@gmail.com");
        when(clientRepository.save(client)).thenReturn(client);
    }

    @Test
    @DisplayName("Should throw an exception when trying to get a client by account number that doesn't exists")
    void getClientByAccountNumberWhenClientNotFound() {
        when(clientRepository.getClientByAccountNumber(324)).thenReturn(null);
        assertThrows(EntityNotFound.class, () -> clientService.getClientByAccountNumber(324));
    }

    @Test
    @DisplayName("Should update a client")
    void updateClient() {
        Client client = new Client(1, "Bruno", 324, 1000.0, ClientRole.CLIENT, "password123", "bruno@gmail.com");

        Client newClient = new Client(2, "Marcos", 324, 2000.0, ClientRole.ADMIN, "password123", "marcos@gmail.com");

        when(clientRepository.getClientByAccountNumber(324)).thenReturn(client);
        clientService.updateClient(newClient);
        assertEquals(2000.0, client.getBalance());
        assertEquals("Marcos", client.getName());
        assertEquals(324, client.getAccountNumber());
    }

}
