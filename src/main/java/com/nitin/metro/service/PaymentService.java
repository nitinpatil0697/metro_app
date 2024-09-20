package com.nitin.metro.service;

import com.nitin.metro.repository.Payment.PaymentTransactionLogRepositoryInterface;
import com.nitin.metro.repository.user.UserRepository;
import com.nitin.metro.repository.vendingMachine.TicketRepositoryInterface;
import com.nitin.metro.api.request.ConfirmPayment;
import com.nitin.metro.api.request.InitiatePaymentRequest;
import com.nitin.metro.constants.AppConstants;
import com.nitin.metro.model.payment.PaymentTransactionLog;
import com.nitin.metro.model.user.User;
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

@Service
public class PaymentService {

    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());
    private final UserRepository userRepository;
    private final PaymentTransactionLogRepositoryInterface paymentTransactionLogRepositoryInterface;


    public PaymentService(UserService userService, UserRepository userRepository, TicketRepositoryInterface ticketRepositoryInterface, PaymentTransactionLogRepositoryInterface paymentTransactionLogRepositoryInterface) {
        this.userRepository = userRepository;
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
            LOGGER.info("initiatePayment : paymentIntent : RES :" + paymentIntent.toString());

            PaymentTransactionLog existingLog = paymentTransactionLogRepositoryInterface.findByTicketId(initiatePaymentRequest.getTicketDetails().getId());

            if (existingLog == null) {
                PaymentTransactionLog paymentTransactionLog = new PaymentTransactionLog();
                paymentTransactionLog.setPaymentStatus(false);
                paymentTransactionLog.setTransactionId(paymentIntent.getId());
                paymentTransactionLog.setTransactionAmount(initiatePaymentRequest.getAmount());
                paymentTransactionLog.setTransactionDate(currentDate);
                paymentTransactionLog.setPaymentId("pay" + UUID.randomUUID().toString().replace("-", ""));
                paymentTransactionLog.setTicketId(initiatePaymentRequest.getTicketDetails().getId());
                paymentTransactionLog.setEmail(reqUser.getEmail());
                paymentTransactionLogRepositoryInterface.save(paymentTransactionLog);
            }

            LOGGER.info("initiatePayment : Saved payment transaction log.");
            responseData.put("id", paymentIntent.getId());
            responseData.put("clientSecret", paymentIntent.getClientSecret());
            responseData.put("customer", paymentIntent.getCustomer());
        } catch (StripeException stripeException) {
            LOGGER.severe("Error initiating payment: " + stripeException.getMessage());
        }

        LOGGER.info("initiatePayment : Payment initiated Successfully");
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

    public String confirmPayment(ConfirmPayment confirmPayment) {
        LOGGER.info("confirmPayment Called." + confirmPayment.toString());
        PaymentTransactionLog paymentTransactionLog = paymentTransactionLogRepositoryInterface.findByTicketId(confirmPayment.getTicketId());
        paymentTransactionLog.setPaymentStatus(confirmPayment.getPaymentCapture());
        paymentTransactionLog.setConfirmResponse(confirmPayment.getPaymentConfirmResponse());
        paymentTransactionLogRepositoryInterface.save(paymentTransactionLog);

        LOGGER.info("confirmPayment : Updated payment transaction log successfully.");
        return "Successfully confirmed payment: ";
    }

    public String getPaymentId(Integer ticketId) {
        PaymentTransactionLog paymentTransactionLog = paymentTransactionLogRepositoryInterface.findByTicketId(ticketId);
        return paymentTransactionLog.getPaymentId();
    }
}
