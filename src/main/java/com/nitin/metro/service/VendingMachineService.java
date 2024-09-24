package com.nitin.metro.service;

import com.nitin.metro.api.response.GeneralResponse;
import com.nitin.metro.constants.AppConstants;
import com.nitin.metro.repository.vendingMachine.RouteRepositoryInterface;
import com.nitin.metro.repository.vendingMachine.StationRepositoryInterface;
import com.nitin.metro.repository.vendingMachine.TicketFareRepositoryInterface;
import com.nitin.metro.repository.vendingMachine.TicketRepositoryInterface;
import com.nitin.metro.api.request.GenerateTicketRequest;
import com.nitin.metro.api.response.GenerateTicketResponse;
import com.nitin.metro.model.vendingMachine.MetroStation;
import com.nitin.metro.model.vendingMachine.Route;
import com.nitin.metro.model.vendingMachine.Ticket;
import com.nitin.metro.model.vendingMachine.TicketFare;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;

@Service
public class VendingMachineService {

    static final String STATUS_SUCCESS = "success";
    static final String STATUS_FAILED = "failed";
    private static final Logger LOGGER = Logger.getLogger(VendingMachineService.class.getName());

    @Autowired
    RouteRepositoryInterface routeRepositoryInterface;

    @Autowired
    TicketRepositoryInterface ticketRepositoryInterface;

    @Autowired
    StationRepositoryInterface stationRepositoryInterface;

    @Autowired
    TicketFareRepositoryInterface ticketFareRepositoryInterface;

    /**
     * To get All the Users
     */
    public ResponseEntity<List<Route>> getAllRoutes() {
        LOGGER.info("API : getAllRoutes called.");
        List<Route> routeData = routeRepositoryInterface.findAll();
        LOGGER.info("API : getAllRoutes fetched successfully.");
        return new ResponseEntity<>(routeData, HttpStatus.OK);
    }

    /**
     * To get All the Tickets
     */
    public ResponseEntity<List<Ticket>> getAllTickets() {
        LOGGER.info("API : getAllTickets called.");
        List<Ticket> ticketData = ticketRepositoryInterface.findAll();
        LOGGER.info("API : getAllTickets fetched successfully.");
        return new ResponseEntity<>(ticketData, HttpStatus.OK);
    }

    /**
     * To get All the Stations
     */
    public ResponseEntity<List<MetroStation>> getAllStations() {
        LOGGER.info("API : getAllStations called.");
        List<MetroStation> stationData = stationRepositoryInterface.findAll();
        LOGGER.info("API : getAllStations fetched successfully.");
        return new ResponseEntity<>(stationData, HttpStatus.OK);
    }

    /**
     * To get All the Ticket Fares
     */
    public ResponseEntity<List<TicketFare>> getAllTicketFares() {
        LOGGER.info("API : getAllTicketFares called.");
        List<TicketFare> ticketFareData = ticketFareRepositoryInterface.findAll();
        LOGGER.info("API : getAllTicketFares fetched successfully.");
        return new ResponseEntity<>(ticketFareData, HttpStatus.OK);
    }

    /**
     * All the Tickets by UserName
     */
    public ResponseEntity<List<Ticket>> getAllTicketsByUserName(String userName) {
        LOGGER.info("API : getAllTickets called.");
        List<Ticket> userPurchasedTicket = ticketRepositoryInterface.findByUserName(userName);
        LOGGER.info("API : getAllTickets fetched successfully.");
        return new ResponseEntity<>(userPurchasedTicket, HttpStatus.OK);

    }

    /**
     * To generate Metro ticket
     */
    public ResponseEntity<GenerateTicketResponse> generateMetroTicket(GenerateTicketRequest generateTicketRequest) {
        LOGGER.info("API : generateMetroTicket called.");
        Ticket generatedTicket = new Ticket();
        GenerateTicketResponse generateTicketResponse = new GenerateTicketResponse();
        try {
            List<TicketFare> ticketFareList = ticketFareRepositoryInterface.
                    findByRouteNameAndTicketTypeAndStartCodeAndEndCode(
                            generateTicketRequest.getRouteName(), generateTicketRequest.getTicketType(),
                            generateTicketRequest.getStartStation(), generateTicketRequest.getEndStation()
                            );
            if (ticketFareList.isEmpty()) {
               throw new Exception("Invalid Route & Ticket.");
            }
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
            generateTicketResponse.setStatus(STATUS_SUCCESS);
            generateTicketResponse.setMessage("Ticket generated successfully.");
            generateTicketResponse.setResult(generatedTicket);
        } catch (Exception e) {
            generateTicketResponse.setStatus(STATUS_FAILED);
            generateTicketResponse.setMessage("Error while generating ticket." + e.getMessage());
            generateTicketResponse.setResult(generatedTicket);
            LOGGER.severe("API : generateMetroTicket : Error : " + e.getMessage());
        }

        LOGGER.info("API : generateMetroTicket Fetched successfully.");
        return new ResponseEntity<>(generateTicketResponse, HttpStatus.OK);

    }

    /**
     * To get All Tickets Users
     */
    public ResponseEntity<List<String>> getAllTicketsUsers() {
        LOGGER.info("API : getAllTicketsUsers called.");
        List<String> users = ticketRepositoryInterface.findUsers();
        LOGGER.info("API : getAllTicketsUsers fetched successfully.");
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    public ResponseEntity<Map<String, String>> getStationsByRoute(String route) {
        LOGGER.info("API : getStationsByRoute called.");
        Map<String, String> stationMap = new HashMap<>();
        List<Route> routes = routeRepositoryInterface.findByName(route);

        for (Route routeData : routes) {
            List<MetroStation> stations = routeData.getStations();
            for (MetroStation station : stations) {
                stationMap.put(station.getCode(), station.getName());
            }
        }

        LOGGER.info("API : getStationsByRoute fetched successfully.");
        return new ResponseEntity<>(stationMap, HttpStatus.OK);
    }

    public ResponseEntity<Integer> getFareByStations(String startCode, String endCode) {
        Integer routeFare = ticketFareRepositoryInterface.findByStartCodeAndEndCode(startCode, endCode);
        LOGGER.info("API : getFareByStations fetched successfully." + routeFare);
        return new ResponseEntity<>(routeFare, HttpStatus.OK);
    }

    public ResponseEntity<GeneralResponse> updateTicketFare(Integer ticketId , TicketFare updatedTicketFare) {
        GeneralResponse response = new GeneralResponse();
        Optional<TicketFare> ticketFares = ticketFareRepositoryInterface.findById(ticketId);

        if (ticketFares.isPresent()) {
            TicketFare existingFare = ticketFares.get();
            existingFare.setFare(updatedTicketFare.getFare());
            existingFare.setTicketType(updatedTicketFare.getTicketType());
            existingFare.setStartCode(updatedTicketFare.getStartCode());
            existingFare.setEndCode(updatedTicketFare.getEndCode());
            existingFare.setFare(updatedTicketFare.getFare());

            ticketFareRepositoryInterface.save(existingFare);
        }
        Optional<TicketFare> updatedTicketFares = ticketFareRepositoryInterface.findById(ticketId);
        TicketFare updatedTicket = updatedTicketFares.get();
        response.setStatus(AppConstants.SUCCESS);
        response.setMessage("Updated Data successfully");
        response.setResult(updatedTicket);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
