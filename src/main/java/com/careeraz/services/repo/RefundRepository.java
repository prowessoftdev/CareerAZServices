package com.careeraz.services.repo;


import com.careeraz.services.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefundRepository extends JpaRepository<Refund, UUID> {

    List<Refund> findByUser(User user);

    List<Refund> findByUserId(Long userId);

    List<Refund> findByPaymentTransaction(PaymentTransaction tx);

    List<Refund> findByStatus(RefundStatus status);

    Optional<Refund> findByStripeRefundId(String stripeRefundId);
}

