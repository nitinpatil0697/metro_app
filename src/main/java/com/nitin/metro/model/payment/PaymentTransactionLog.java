package com.nitin.metro.model.payment;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class PaymentTransactionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String paymentId;
    private Boolean paymentStatus;
    private String transactionId;
    private Date transactionDate;
    private long transactionAmount;
    private int ticketId;
    private String confirmResponse;
}
