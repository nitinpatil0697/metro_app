package com.nitin.metro.api.request;


import com.nitin.metro.model.vendingMachine.Ticket;
import lombok.Data;

@Data
public class InitiatePaymentRequest {
    private Long amount;
    private String currency;
    private Ticket ticketDetails;
}
