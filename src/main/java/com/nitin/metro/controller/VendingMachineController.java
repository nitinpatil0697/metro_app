package com.nitin.metro.controller;

import com.nitin.metro.model.*;
import com.nitin.metro.service.VendingMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("vendingMachine")
public class VendingMachineController {

    @Autowired
    VendingMachineService vendingMachineService;

    @GetMapping("allRoutes")
    public ResponseEntity<List<Route>> getAllRoutes() {
        return vendingMachineService.getAllRoutes();
    }

    @GetMapping("allTickets")
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return vendingMachineService.getAllTickets();
    }

    @GetMapping("allTickets/{userName}")
    public ResponseEntity<List<Ticket>> getAllTicketsByUserName(@PathVariable String userName) {
        return vendingMachineService.getAllTicketsByUserName(userName);
    }

    @GetMapping("allTickets/users")
    public ResponseEntity<List<String>> getAllTicketsUsers() {
        return vendingMachineService.getAllTicketsUsers();
    }

    @GetMapping("allStations")
    public ResponseEntity<List<MetroStation>> getAllStations() {
        return vendingMachineService.getAllStations();
    }

    @GetMapping("allTicketFare")
    public ResponseEntity<List<TicketFare>> getAllTicketFares() {
        return vendingMachineService.getAllTicketFares();
    }

    @PostMapping("generateTicket")
    public ResponseEntity<GenerateTicketResponse> generateTicket(@RequestBody GenerateTicketRequest generateTicketRequest)
    {
        return vendingMachineService.generateMetroTicket(generateTicketRequest);
    }
}