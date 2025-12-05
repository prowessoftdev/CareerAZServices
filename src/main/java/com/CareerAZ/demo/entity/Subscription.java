package com.CareerAZ.demo.entity;



import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "subscriptions",
        indexes = {@Index(name = "idx_sub_user", columnList = "user_id")})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Plan key: FREE, MONTHLY, YEARLY, ENTERPRISE
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Plan plan;

    // Stripe identifiers (don't store card data)
    private String stripeCustomerId;
    private String stripeSubscriptionId;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;

    private Instant startedAt;
    private Instant trialEndsAt;
    private Instant currentPeriodStart;
    private Instant currentPeriodEnd;
    private Instant canceledAt;
    private Instant createdAt;
    private Instant updatedAt;

    @PrePersist
    public void prePersist() { createdAt = Instant.now(); }
    @PreUpdate
    public void preUpdate() { updatedAt = Instant.now(); }
}

