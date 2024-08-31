package com.nitin.metro.model.vendingMachine;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String route;
    private Integer fare;
    private String type;
    @JsonProperty("purchase_time")
    private Date purchaseTime;
    @JsonProperty("user_name")
    private String userName;
    private boolean paymentCapture;
}
