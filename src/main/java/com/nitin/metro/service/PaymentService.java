package com.nitin.metro.service;

import com.nitin.metro.api.request.InitiatePaymentRequest;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class PaymentService {

    private static final Logger LOGGER = Logger.getLogger(PaymentService.class.getName());

    /**
     * To initiate Payment
     */
    public ResponseEntity<Map<String, String>> initiatePayment(InitiatePaymentRequest initiatePaymentRequest) throws StripeException {
        Map<String, String> responseData = new HashMap<>();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("amount", initiatePaymentRequest.getAmount());
            params.put("currency", initiatePaymentRequest.getCurrency());
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            responseData.put("clientSecret", paymentIntent.getClientSecret());
        } catch (StripeException stripeException) {
            LOGGER.severe("Error initiating payment: " + stripeException.getMessage());
        }

        LOGGER.info("Client secret: " + responseData.get("clientSecret"));
        return new ResponseEntity<>(responseData, HttpStatus.CREATED);
    }
}
