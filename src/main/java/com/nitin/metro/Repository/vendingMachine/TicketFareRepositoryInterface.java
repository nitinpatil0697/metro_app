package com.nitin.metro.Repository.vendingMachine;

import com.nitin.metro.model.vendingMachine.TicketFare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketFareRepositoryInterface extends JpaRepository<TicketFare, Integer> {

    List<TicketFare> findByRouteNameAndTicketType(String routeName, String ticketType);

    @Query("SELECT tf.fare FROM TicketFare tf WHERE tf.startCode = :startCode AND tf.endCode = :endCode")
    Integer findByStartCodeAndEndCode(@Param("startCode") String startCode, @Param("endCode") String endCode);
}
