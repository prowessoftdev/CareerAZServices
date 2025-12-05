package com.CareerAZ.demo.entity;



import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "refunds",
        indexes = {
                @Index(name = "idx_refund_payment_tx", columnList = "payment_transaction_id"),
                @Index(name = "idx_refund_stripe", columnList = "stripe_refund_id")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Refund {

    @Id
    @GeneratedValue
    private UUID id;

    // Link to the original payment transaction (nullable if external)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_transaction_id")
    private PaymentTransaction paymentTransaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Amount refunded
    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal amount;

    private String currency;

    // Reason provided by admin/user
    @Column(length = 1024)
    private String reason;

    // External id from payment gateway (stripe refund id)
    private String stripeRefundId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RefundStatus status = RefundStatus.REQUESTED;

    // Raw payload from gateway webhook (optional)
    @Column(columnDefinition = "jsonb")
    private String rawPayload;

    private Instant requestedAt;
    private Instant processedAt;

    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
        if (requestedAt == null) requestedAt = Instant.now();
    }
}

