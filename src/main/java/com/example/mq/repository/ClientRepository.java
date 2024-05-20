package com.example.mq.repository;

import com.example.mq.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Repository for Client entity
 * Extends JpaRepository to have access to CRUD operations and create custom queries
 */
public interface ClientRepository extends JpaRepository<Client, Integer>  {

    /**
     * Custom query to get a client by account number
     * @param accountNumber the account number of the client
     */
    Client getClientByAccountNumber(Integer accountNumber);

    Client getClientByEmail(String email);

    UserDetails findByEmail(String email);
}
