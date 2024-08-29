package com.nitin.metro.api.request;

import com.nitin.metro.model.vendingMachine.Ticket;
import lombok.Data;

@Data
public class InitiatePaymentRequest {
    private Long amount;
    private String currency;
    private Ticket ticketDetails;

    @Override
    public String toString() {
        return "InitiatePaymentRequest{" +
                "amount=" + amount +
                ", currency='" + currency + '\'' +
                ", ticketDetails=" + ticketDetails +
                '}';
    }
}
