package com.example.mq.controller;


import com.example.mq.controller.DTO.LoginDTO;
import com.example.mq.controller.DTO.TokenDTO;
import com.example.mq.model.Client;
import com.example.mq.model.ClientRole;
import com.example.mq.service.ClientService;
import com.example.mq.service.TokenService;
import com.example.mq.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for client operations
 * Annotations:
 * - @RestController: Indicates that an annotated class is a "Controller"
 * - @RequestMapping: Maps web requests to Spring Controller methods
 * - @PostMapping: Maps HTTP POST requests onto specific handler methods
 * - @PutMapping: Maps HTTP PUT requests onto specific handler methods
 */
@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TokenService tokenService;

    /**
     * Get all clients
     */
    @GetMapping()
    public ResponseEntity<List<Client>> getAllClients() {
        return ResponseEntity.ok().body(clientService.getAllClients());
    }

    /**
     * Get a client by account number
     * @param accountNumber account number
     */
    @GetMapping("/{accountNumber}")
    public ResponseEntity<Client> getClientByAccountNumber(@PathVariable Integer accountNumber) {
        return ResponseEntity.ok().body(clientService.getClientByAccountNumber(accountNumber));
    }


    /**
     * Login endpoint
     * @param client login information
     */
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDTO client) {
        var loginPassword = new UsernamePasswordAuthenticationToken(client.email(), client.password());
        var auth = this.authenticationManager.authenticate(loginPassword);
        var token = tokenService.generateToken((Client) auth.getPrincipal());
        return ResponseEntity.ok(new TokenDTO(token));
    }

    /**
     * Create a new client
     * @param client client object
     */
    @PostMapping("/register")
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        String encodedPassword = new BCryptPasswordEncoder().encode(client.getPassword());
        client.setPassword(encodedPassword);
        return ResponseEntity.ok().body(clientService.createClient(client));
    }

    /**
     * Update a client
     * @param client client
     */
    @PutMapping("/{accountNumber}")
    public ResponseEntity<Client> updateClient(@PathVariable Integer accountNumber, @RequestBody Client client) {
        Client clientToUpdate = clientService.getClientByAccountNumber(accountNumber);
        client.setId(clientToUpdate.getId());
        return ResponseEntity.ok().body(clientService.updateClient(client));
    }


    /**
     * Delete a client
     * @param accountNumber account number
     */
    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<Void> deleteClient(@PathVariable Integer accountNumber) {
        clientService.deleteClient(accountNumber);
        return ResponseEntity.ok().build();
    }


}
