package com.nitin.metro.controller;

import com.nitin.metro.api.request.ConfirmPayment;
import com.nitin.metro.api.request.InitiatePaymentRequest;
import com.nitin.metro.service.PaymentService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("payment")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @PostMapping("initiate")
    public ResponseEntity<Map<String,Object>> initiatePayment(@RequestBody InitiatePaymentRequest initiatePaymentRequest) throws StripeException {
        return paymentService.initiatePayment(initiatePaymentRequest);
    }

    @PostMapping("confirm")
    public String confirmPayment(@RequestBody ConfirmPayment confirmPayment) {
        return paymentService.confirmPayment(confirmPayment);
    }

    @GetMapping("transaction/{ticketId}")
    public String getPaymentId(@PathVariable Integer ticketId) {
        return paymentService.getPaymentId(ticketId);

    }
}
