package com.CareerAZ.demo.entity;



import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "invoices",
        indexes = {@Index(name = "idx_invoice_user", columnList = "user_id")})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String stripeInvoiceId;

    @Column(precision = 12, scale = 2)
    private BigDecimal amount; // store in decimal for safety (currency minor units also ok)

    private String currency; // "usd"

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    private Instant issuedAt;
    private Instant paidAt;
    private Instant createdAt;

    @Column(columnDefinition = "jsonb")
    private String rawStripePayload; // store webhook payload for auditing (optional)

    @PrePersist
    public void prePersist() { createdAt = Instant.now(); }
}

