package com.nitin.metro.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class TicketFare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String routeName;
    private String ticketType;
    private Date slotStart;
    private Date slotEnd;
    private int fare;
}
