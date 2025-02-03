package com.example.eval.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.eval.model.Client;
import com.example.eval.repository.ClientRepository;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    // Method to get all clients
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    // Method to add or update a client
    public void addClient(Client client) {
        clientRepository.save(client);
    }

    // Method to get a client by email
    public Client getClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    // Method to delete a client by email
    public void deleteClientByEmail(String email) {
        Client client = clientRepository.findByEmail(email);
        if (client != null) {
            clientRepository.delete(client);
        }
    }

    // Method to sort clients by age
    public List<Client> sortClientsByAge() {
        return clientRepository.findAll().stream()
                .sorted((c1, c2) -> c2.getBirthDate().compareTo(c1.getBirthDate()))
                .collect(Collectors.toList());
    }
}
