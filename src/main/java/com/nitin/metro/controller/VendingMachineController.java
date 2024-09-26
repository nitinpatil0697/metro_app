package com.nitin.metro.controller;

import com.nitin.metro.api.request.GenerateTicketRequest;
import com.nitin.metro.api.response.GeneralResponse;
import com.nitin.metro.api.response.GenerateTicketResponse;
import com.nitin.metro.model.vendingMachine.MetroStation;
import com.nitin.metro.model.vendingMachine.Route;
import com.nitin.metro.model.vendingMachine.Ticket;
import com.nitin.metro.model.vendingMachine.TicketFare;
import com.nitin.metro.service.VendingMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<GenerateTicketResponse> generateTicket(@RequestBody GenerateTicketRequest generateTicketRequest) {
        return vendingMachineService.generateMetroTicket(generateTicketRequest);
    }

    @GetMapping("route/{routeName}")
    public ResponseEntity<Map<String, String>> getStationsByRouteName(@PathVariable String routeName) {
        return vendingMachineService.getStationsByRoute(routeName);
    }

    @GetMapping("calculatefare")
    public ResponseEntity<Integer> getStationsByRouteName(
            @RequestParam("start") String startCode,
            @RequestParam("end") String endCode) {
        return vendingMachineService.getFareByStations(startCode, endCode);
    }

    @PutMapping("updateTicketFare/{ticketId}")
    public ResponseEntity<GeneralResponse> updateTicketFare(
            @PathVariable Integer ticketId, @RequestBody TicketFare updateTicketFare) {
        return vendingMachineService.updateTicketFare(ticketId, updateTicketFare);
    }

    @DeleteMapping("updateTicketFare/{ticketFareId}")
    public ResponseEntity<GeneralResponse> deleteTicketFare(@PathVariable Integer ticketFareId) {
        return vendingMachineService.deleteTicketFare(ticketFareId);
    }
}
