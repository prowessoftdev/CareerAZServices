package com.careeraz.services.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "billing_event_logs",
        indexes = {@Index(name = "idx_billing_event_type", columnList = "event_type")})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillingEventLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventType;
    private String stripeEventId;

    @Column(columnDefinition = "json")
    private String payload;

    private Instant receivedAt;

    @PrePersist
    public void prePersist() {
        receivedAt = Instant.now();
    }
}

