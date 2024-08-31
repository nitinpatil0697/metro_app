package com.nitin.metro.service;

import com.nitin.metro.Repository.Payment.PaymentTransactionLogRepositoryInterface;
import com.nitin.metro.Repository.user.UserRepository;
import com.nitin.metro.Repository.vendingMachine.TicketRepositoryInterface;
import com.nitin.metro.api.request.InitiatePaymentRequest;
import com.nitin.metro.constants.AppConstants;
import com.nitin.metro.model.payment.PaymentTransactionLog;
import com.nitin.metro.model.user.User;
import com.nitin.metro.model.vendingMachine.Ticket;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;
import com.nitin.metro.api.request.PaymentRequest;

@Service
public class PaymentService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final TicketRepositoryInterface ticketRepositoryInterface;
    private final PaymentTransactionLogRepositoryInterface paymentTransactionLogRepositoryInterface;
    PaymentRequest paymentRequest;
    private static final Logger LOGGER = Logger.getLogger(PaymentService.class.getName());

    public PaymentService(UserService userService, UserRepository userRepository, TicketRepositoryInterface ticketRepositoryInterface, PaymentTransactionLogRepositoryInterface paymentTransactionLogRepositoryInterface) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.ticketRepositoryInterface = ticketRepositoryInterface;
        this.paymentTransactionLogRepositoryInterface = paymentTransactionLogRepositoryInterface;
    }

    /**
     * To initiate Payment
     */
    public ResponseEntity<Map<String, Object>> initiatePayment(InitiatePaymentRequest initiatePaymentRequest) throws StripeException {
        Map<String, Object> responseData = new HashMap<>();
        try {
            LOGGER.info("initiatePayment : REQ :" + initiatePaymentRequest.toString());
            Date currentDate = new Date();
            User reqUser = userRepository.findByEmail(initiatePaymentRequest.getTicketDetails().getUserName());
            Customer customer = this.createCustomer(reqUser);
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(initiatePaymentRequest.getAmount())
                    .setCurrency(initiatePaymentRequest.getCurrency())
                    .setCustomer(customer.getId())
                    .setDescription(AppConstants.PAYMENT_PROCESS)
                    .putMetadata("address", reqUser.getAddress().toString())
                    .build();
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            PaymentTransactionLog paymentTransactionLog = new PaymentTransactionLog();
            paymentTransactionLog.setPaymentStatus(false);
            paymentTransactionLog.setTransactionId(paymentIntent.getId());
            paymentTransactionLog.setTransactionAmount(initiatePaymentRequest.getAmount());
            paymentTransactionLog.setTransactionDate(currentDate);
            paymentTransactionLog.setTicketId(initiatePaymentRequest.getTicketDetails().getId());
            paymentTransactionLogRepositoryInterface.save(paymentTransactionLog);
            responseData.put("id", paymentIntent.getId());
            responseData.put("clientSecret", paymentIntent.getClientSecret());
            responseData.put("customer", paymentIntent.getCustomer());
        } catch (StripeException stripeException) {
            LOGGER.severe("Error initiating payment: " + stripeException.getMessage());
        }
        
        return new ResponseEntity<>(responseData, HttpStatus.CREATED);
    }


    public Customer createCustomer(User reqUser) throws StripeException {
        CustomerCreateParams params = CustomerCreateParams.builder()
                .setName(reqUser.getFirstName())
                .setEmail(reqUser.getEmail())
                .build();
        try {
            return Customer.create(params);
        } catch (Exception e) {
            throw new RuntimeException("Error creating customer: " + e.getMessage());
        }

    }
}