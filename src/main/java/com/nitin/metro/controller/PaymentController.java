package com.nitin.metro.controller;

import com.nitin.metro.api.request.InitiatePaymentRequest;
import com.nitin.metro.service.PaymentService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
