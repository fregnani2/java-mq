package com.example.mq.service;

import com.example.mq.model.Client;
import com.example.mq.service.exceptions.DuplicateAccount;
import com.example.mq.service.exceptions.EntityNotFound;
import com.example.mq.service.exceptions.WrongArgument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.mq.repository.ClientRepository;

import java.util.List;

/**
 * Service for client operations
 * Annotations:
 * - @Service: Indicates that an annotated class is a "Service"
 * - @Autowired: Marks a constructor, field, setter method, or config method as to be autowired by Spring's dependency injection facilities.
 */
@Service
public class ClientService {
    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);

    @Autowired
    private ClientRepository clientRepository;


    /**
     * Method to create a client
     * @param obj Client object to create
     * @throws DuplicateAccount if the account number already exists
     * @throws WrongArgument if the name is null or empty or the balance is negative or null
     */
    public Client createClient(Client obj) {
        logger.info("Creating client {}",obj.getName());
        if (clientRepository.getClientByAccountNumber(obj.getAccountNumber()) != null) {
            throw new DuplicateAccount("Account number already exists");
        }
        if (clientRepository.getClientByEmail(obj.getEmail()) != null) {
            throw new DuplicateAccount("Email already exists");
        }
        if(obj.getName() == null || obj.getName().isEmpty()){
            throw new WrongArgument("Name cannot be null or empty");
        }
        if (obj.getBalance()== null || obj.getBalance() < 0) {
            throw new WrongArgument("Balance cannot be negative or null");
        }
        logger.info("Client created");
        return clientRepository.save(obj);
    }

    /**
     * Method to get a client by account number
     * @param accountNumber the account number of the client to get
     */
    public Client getClientByAccountNumber(Integer accountNumber) {
        logger.info("Getting client of account number" + accountNumber);
        if (clientRepository.getClientByAccountNumber(accountNumber) == null) {
            throw new EntityNotFound("Client of account number " + accountNumber + " not found");
        }
        logger.info("Client found");
        return clientRepository.getClientByAccountNumber(accountNumber);
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    /**
     * Method to update a client
     * @param obj Client object to update
     */
    public Client updateClient(Client obj){
        logger.info("Updating client {}", obj.getName());
        Client client = clientRepository.getClientByAccountNumber(obj.getAccountNumber());
        if(client == null){
            throw new EntityNotFound("Client not found");
        }
        client.setName(obj.getName());
        client.setBalance(obj.getBalance());
        logger.info("Client updated");
        return clientRepository.save(client);
    }

    /**
     * Method to delete a client
     * @param accountNumber the account number of the client to delete
     */
    public Client deleteClient(Integer accountNumber){
        logger.info("Deleting client of account number" + accountNumber);
        Client client = clientRepository.getClientByAccountNumber(accountNumber);
        if(client == null){
            throw new EntityNotFound("Client not found");
        }
        logger.info("Client deleted");
        clientRepository.delete(client);
        return client;
    }


}
