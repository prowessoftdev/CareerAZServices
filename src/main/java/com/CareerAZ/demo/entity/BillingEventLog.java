package com.CareerAZ.demo.entity;



import jakarta.persistence.*;
import lombok.*;
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
    @GeneratedValue
    private UUID id;

    private String eventType;
    private String stripeEventId;

    @Column(columnDefinition = "jsonb")
    private String payload;

    private Instant receivedAt;

    @PrePersist
    public void prePersist() { receivedAt = Instant.now(); }
}

