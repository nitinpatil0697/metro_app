package com.nitin.metro.Repository.Payment;

import com.nitin.metro.model.payment.PaymentTransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentTransactionLogRepositoryInterface extends JpaRepository<PaymentTransactionLog, Long> {

}
