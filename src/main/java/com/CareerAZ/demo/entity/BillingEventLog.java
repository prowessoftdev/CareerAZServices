package com.CareerAZ.demo.entity;



import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

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
    public void prePersist() { receivedAt = Instant.now(); }
}

