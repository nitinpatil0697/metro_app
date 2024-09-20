package com.nitin.metro.repository.Payment;

import com.nitin.metro.model.payment.PaymentTransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTransactionLogRepositoryInterface extends JpaRepository<PaymentTransactionLog, Long> {

    PaymentTransactionLog findByTicketId(Integer ticketId);

//    @Modifying
//    @Transactional
//    @Query("UPDATE PaymentTransactionLog p SET p.paymentStatus = :paymentCapture , p.confirmResponse = :paymentConfirmResponse WHERE p.ticketId = :ticketId")
//    void updatePaymentTransaction(Integer ticketId, Boolean paymentCapture, String paymentConfirmResponse);

}
