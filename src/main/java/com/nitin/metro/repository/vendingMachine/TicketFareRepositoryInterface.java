package com.nitin.metro.repository.vendingMachine;

import com.nitin.metro.model.vendingMachine.TicketFare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketFareRepositoryInterface extends JpaRepository<TicketFare, Integer> {

    List<TicketFare> findByRouteNameAndTicketTypeAndStartCodeAndEndCode(
            String routeName, String ticketType, String startCode, String endCode);

    @Query("SELECT tf.fare FROM TicketFare tf WHERE tf.startCode = :startCode AND tf.endCode = :endCode")
    Integer findByStartCodeAndEndCode(@Param("startCode") String startCode, @Param("endCode") String endCode);
}
