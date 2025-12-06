package com.CareerAZ.entity;


import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "payment_methods",
        uniqueConstraints = {@UniqueConstraint(name = "uq_paymentmethod_stripe", columnNames = {"stripe_payment_method_id"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String stripePaymentMethodId; // e.g., pm_...

    private String cardBrand; // Visa, Mastercard
    private String cardLast4;
    private int cardExpMonth;
    private int cardExpYear;

    private boolean isDefault;

    private Instant createdAt;
    private Instant updatedAt;

    @PrePersist
    public void prePersist() { createdAt = Instant.now(); }
    @PreUpdate
    public void preUpdate() { updatedAt = Instant.now(); }
}
