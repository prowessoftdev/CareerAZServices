package com.careeraz.services.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "coupons",
        indexes = {
                @Index(name = "idx_coupon_code", columnList = "code"),
                @Index(name = "idx_coupon_active", columnList = "active")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_coupon_code", columnNames = {"code"})
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64, unique = true)
    private String code; // e.g., "WELCOME25"

    private String description;

    // Discount type: either percent or fixed amount
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponType type;

    // For PERCENT: value between 0-100. For AMOUNT: currency minor units or decimal.
    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    // Currency for fixed amount (e.g., "usd"). Nullable for percent coupons.
    private String currency;

    // Optional maximum discount cap (for percent coupons)
    @Column(precision = 12, scale = 2)
    private BigDecimal maxDiscountAmount;

    // Plan constraints: JSON array of plan keys or null for all plans.
    @Column(columnDefinition = "json")
    private String appliesToPlansJson;

    private Integer maxRedemptions;      // null = unlimited
    private Integer redeemedCount = 0;   // increment on redemption

    private Instant validFrom;
    private Instant validUntil;

    private boolean active = true;

    private Instant createdAt;
    private Instant updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }
}

