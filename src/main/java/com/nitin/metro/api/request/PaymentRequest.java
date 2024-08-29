package com.nitin.metro.api.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentRequest {
    private long amount;
    private String currency;
    private String description;
    private String addressJson; // JSON string of the address
}
