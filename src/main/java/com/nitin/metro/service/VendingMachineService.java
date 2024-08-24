package com.nitin.metro.service;

import com.nitin.metro.Repository.RouteRepositoryInterface;
import com.nitin.metro.Repository.StationRepositoryInterface;
import com.nitin.metro.Repository.TicketFareRepositoryInterface;
import com.nitin.metro.Repository.TicketRepositoryInterface;
import com.nitin.metro.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Date;
import java.util.logging.Logger;

import java.util.List;

@Service
public class VendingMachineService {
    private static final Logger LOGGER = Logger.getLogger(VendingMachineService.class.getName());

    @Autowired
    RouteRepositoryInterface routeRepositoryInterface;

    @Autowired
    TicketRepositoryInterface ticketRepositoryInterface;

    @Autowired
    StationRepositoryInterface stationRepositoryInterface;

    @Autowired
    TicketFareRepositoryInterface ticketFareRepositoryInterface;

    public ResponseEntity<List<Route>> getAllRoutes() {
        LOGGER.info("API : getAllRoutes called.");
        List<Route> routeData = routeRepositoryInterface.findAll();
        LOGGER.info("API : getAllRoutes fetched successfully.");
        return new ResponseEntity<>(routeData, HttpStatus.OK);
    }

    public ResponseEntity<List<Ticket>> getAllTickets() {
        LOGGER.info("API : getAllTickets called.");
        List<Ticket> ticketData = ticketRepositoryInterface.findAll();
        LOGGER.info("API : getAllTickets fetched successfully.");
        return new ResponseEntity<>(ticketData, HttpStatus.OK);
    }

    public ResponseEntity<List<MetroStation>> getAllStations() {
        LOGGER.info("API : getAllStations called.");
        List<MetroStation> stationData = stationRepositoryInterface.findAll();
        LOGGER.info("API : getAllStations fetched successfully.");
        return new ResponseEntity<>(stationData, HttpStatus.OK);
    }

    public ResponseEntity<List<TicketFare>> getAllTicketFares() {
        LOGGER.info("API : getAllTicketFares called.");
        List<TicketFare> ticketFareData = ticketFareRepositoryInterface.findAll();
        LOGGER.info("API : getAllTicketFares fetched successfully.");
        return new ResponseEntity<>(ticketFareData, HttpStatus.OK);
    }

    public ResponseEntity<List<Ticket>> getAllTicketsByUserName(String userName) {
        LOGGER.info("API : getAllTickets called.");
        List<Ticket> userPurchasedTicket = ticketRepositoryInterface.findByUserName(userName);
        LOGGER.info("API : getAllTickets fetched successfully.");
        return new ResponseEntity<>(userPurchasedTicket, HttpStatus.OK);

    }

    public ResponseEntity<Ticket> generateMetroTicket(GenerateTicketRequest generateTicketRequest) {
        LOGGER.info("API : generateMetroTicket called.");
        Ticket generatedTicket = new Ticket();
        try {
            List<TicketFare> ticketFareList = ticketFareRepositoryInterface.findByRouteNameAndTicketType(generateTicketRequest.getRouteName(), generateTicketRequest.getTicketType());
            TicketFare ticketFare = ticketFareList.get(0);
            if (generateTicketRequest.getPeakHour()) {
                int newPeakHourFare = (int) (ticketFare.getFare() * 1.50);
                ticketFare.setFare(newPeakHourFare);
            }
            Date currentTime = new Date();
            generatedTicket.setRoute(ticketFare.getRouteName());
            generatedTicket.setType(ticketFare.getTicketType());
            generatedTicket.setFare(ticketFare.getFare());
            generatedTicket.setPurchaseTime(currentTime);
            generatedTicket.setUserName(generateTicketRequest.getUserName());
            ticketRepositoryInterface.save(generatedTicket);
        } catch (Exception e) {
            LOGGER.severe("API : generateMetroTicket : Error : " + e.getMessage());
        }

        LOGGER.info("API : generateMetroTicket Fetched successfully.");
        return new ResponseEntity<>(generatedTicket, HttpStatus.OK);

    }
}
