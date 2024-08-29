package com.nitin.metro.service;

import com.nitin.metro.api.request.InitiatePaymentRequest;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import com.nitin.metro.api.request.PaymentRequest;

@Service
public class PaymentService {

    PaymentRequest paymentRequest;
    private static final Logger LOGGER = Logger.getLogger(PaymentService.class.getName());

    /**
     * To initiate Payment
     */
    public ResponseEntity<Map<String, Object>> initiatePayment(InitiatePaymentRequest initiatePaymentRequest) throws StripeException {
        Map<String, Object> responseData = new HashMap<>();
        try {
            LOGGER.info("initiatePayment : REQ :" + initiatePaymentRequest.toString());
            Customer customer = this.createCustomer();
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(initiatePaymentRequest.getAmount())  // Amount in cents
                    .setCurrency(initiatePaymentRequest.getCurrency())
                    .setCustomer(customer.getId())
                    .setDescription("test") // Optional
                    .putMetadata("address", "{\n" +
                            "  \"address\": {\n" +
                            "    \"city\": \"kolhapur\",\n" +
                            "    \"country\": \"India\",\n" +
                            "    \"line1\": \"sambhajinagar\",\n" +
                            "    \"line2\": \"karanjakar\",\n" +
                            "    \"postal_code\": \"12343\",\n" +
                            "    \"state\": \"Maharashtra\"\n" +
                            "  }\n" +
                            "}") // Include address JSON
                    .build();
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            responseData.put("clientSecret", paymentIntent.getClientSecret());
            responseData.put("id", paymentIntent.getId());
            responseData.put("customer", paymentIntent.getCustomer());
        } catch (StripeException stripeException) {
            LOGGER.severe("Error initiating payment: " + stripeException.getMessage());
        }
        
        return new ResponseEntity<>(responseData, HttpStatus.CREATED);
    }


    public Customer createCustomer(){
        CustomerCreateParams params = CustomerCreateParams.builder()
                .setName("Nitin")
                .setEmail("nitin@test.com")
                .build();
        try {
            return Customer.create(params);
        } catch (Exception e) {
            throw new RuntimeException("Error creating customer: " + e.getMessage());
        }

    }
}
