package com.nitin.metro.Repository.vendingMachine;

import com.nitin.metro.model.vendingMachine.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepositoryInterface extends JpaRepository<Ticket, Integer> {
    List<Ticket> findByUserName(String userName);

    @Query("SELECT DISTINCT t.userName FROM Ticket t")
    List<String> findUsers();
}
