package com.nitin.metro.Repository.vendingMachine;

import com.nitin.metro.model.vendingMachine.TicketFare;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketFareRepositoryInterface extends JpaRepository<TicketFare, Integer> {

    List<TicketFare> findByRouteNameAndTicketType(String routeName, String ticketType);
}
