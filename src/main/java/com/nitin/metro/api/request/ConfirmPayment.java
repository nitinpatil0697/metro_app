package com.nitin.metro.api.request;

import lombok.Data;

@Data
public class ConfirmPayment {
    private Integer ticketId;
    private Boolean paymentCapture;
    private String paymentConfirmResponse;
}
