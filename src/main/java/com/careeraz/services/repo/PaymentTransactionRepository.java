package com.careeraz.services.repo;


import com.careeraz.services.entity.Invoice;
import com.careeraz.services.entity.PaymentStatus;
import com.careeraz.services.entity.PaymentTransaction;
import com.careeraz.services.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, UUID> {
    List<PaymentTransaction> findByUser(User user);

    List<PaymentTransaction> findByUserId(Long userId);

    List<PaymentTransaction> findByInvoice(Invoice invoice);

    List<PaymentTransaction> findByStatus(PaymentStatus status);

    Optional<PaymentTransaction> findByStripeChargeId(String stripeChargeId);

    Optional<PaymentTransaction> findByStripePaymentIntentId(String intentId);

    List<PaymentTransaction> findTop20ByUserIdOrderByCreatedAtDesc(Long userId);
}

