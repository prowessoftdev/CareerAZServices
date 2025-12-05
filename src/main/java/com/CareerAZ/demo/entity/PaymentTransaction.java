package com.CareerAZ.demo.entity;




import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "payment_transactions",
        indexes = {
                @Index(name = "idx_payment_user", columnList = "user_id"),
                @Index(name = "idx_payment_stripe_charge", columnList = "stripe_charge_id"),
                @Index(name = "idx_payment_status", columnList = "status")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentTransaction {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Optional: invoice associated with this transaction
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    // charge id or payment_intent id from gateway
    @Column(length = 128)
    private String stripeChargeId;

    @Column(length = 128)
    private String stripePaymentIntentId;

    @Column(precision = 12, scale = 2)
    private BigDecimal amount;

    private String currency;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.PENDING;

    private Instant attemptedAt;
    private Instant succeededAt;
    private Instant refundedAt;

    // Raw webhook/response payload for auditing (truncate if huge)
    @Column(columnDefinition = "jsonb")
    private String rawStripePayload;

    // Refunds associated (bi-directional optional)
    @OneToMany(mappedBy = "paymentTransaction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Refund> refunds;

    private Instant createdAt;
    private Instant updatedAt;

    @PrePersist
    public void prePersist() { createdAt = Instant.now(); }

    @PreUpdate
    public void preUpdate() { updatedAt = Instant.now(); }
}
