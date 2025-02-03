package com.example.eval.controller;

import com.example.eval.model.Client;
import com.example.eval.service.ClientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RequestMapping; // This is an import to use rest api methods
// import org.springframework.web.bind.annotation.RestController; // This is an import to use rest api methods

import java.time.LocalDate;
import java.util.List;

// For using rest api methods
// @RestController
// @RequestMapping("/api/clients")
@Controller
public class ClientController {

    @Autowired
    private ClientService clientService;

    // Method to return the HTML page with a list of clients, sorted by age if
    // specified
    @GetMapping("/clients/list")
    public String getClientsList(Model model, @RequestParam(required = false) String sortOrder) {
        List<Client> clients = clientService.getAllClients();

        if (sortOrder != null && sortOrder.equalsIgnoreCase("asc")) {
            clients.sort((client1, client2) -> Integer.compare(
                    client2.getBirthDate().getYear(),
                    client1.getBirthDate().getYear()));
        } else if (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) {
            clients.sort((client1, client2) -> Integer.compare(
                    client1.getBirthDate().getYear(),
                    client2.getBirthDate().getYear()));
        } else {
            clients = clientService.getAllClients();
        }

        model.addAttribute("clients", clients);
        model.addAttribute("sortOrder", sortOrder);
        return "clients/list";
    }

    // Get method for api to get all clients list
    @GetMapping("/api/clients/list")
    public ResponseEntity<List<Client>> getAllClientsApi() {
        List<Client> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    // Method to return the HTML page to add a new client
    @GetMapping("/clients/add")
    public String getAddClientForm(Model model) {
        model.addAttribute("client", new Client());
        return "clients/add";
    }

    // Method to handle the form submission to add a new client
    @PostMapping("/clients/add")
    public String addClient(Client client) {
        client.setRegistrationDate(LocalDate.now());
        clientService.addClient(client);
        return "redirect:/clients/list";
    }

    // Post method for api
    @PostMapping("/add")
    public ResponseEntity<String> addClientApi(@RequestBody Client client) {
        if (client.getEmail() == null || client.getFirstName() == null || client.getLastName() == null
                || client.getBirthDate() == null) {
            return ResponseEntity.badRequest().body("All fields are required.");
        }
        client.setRegistrationDate(LocalDate.now());
        clientService.addClient(client);
        return ResponseEntity.ok("Client added successfully.");
    }

    // Method to delete a client by email
    @GetMapping("/clients/delete/{email}")
    public String deleteClientByEmail(@PathVariable String email) {
        clientService.deleteClientByEmail(email);
        return "redirect:/clients/list";
    }

    // Method to return the HTML page to edit a client by email (GET)
    @GetMapping("/clients/edit/{email}")
    public String editClientByEmail(@PathVariable String email, Model model) {
        Client client = clientService.getClientByEmail(email);
        model.addAttribute("client", client);
        return "clients/edit";
    }

    // Method to save the edited client details (POST)
    @PostMapping("/clients/edit/{email}")
    public String saveClientChanges(@PathVariable String email, Client updatedClient) {
        Client client = clientService.getClientByEmail(email);
        if (client != null) {
            client.setFirstName(updatedClient.getFirstName());
            client.setLastName(updatedClient.getLastName());
            client.setBirthDate(updatedClient.getBirthDate());

            // Automatically set the registration date to the current date
            client.setRegistrationDate(LocalDate.now());

            // Save the updated client to the database
            clientService.addClient(client);
        }

        return "redirect:/clients/list";
    }

    // Method to return the HTML page searched client by email
    @GetMapping("/clients/search")
    public String searchClientByEmail(String email, Model model) {
        Client client = clientService.getClientByEmail(email.trim());
        if (client != null) {
            model.addAttribute("client", client);
            return "clients/searchResult";
        } else {
            model.addAttribute("errorMessage", "Client not found");
            return "clients/searchResult";
        }
    }

}
