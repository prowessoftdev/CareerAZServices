package com.CareerAZ.demo.entity;



import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "audit_logs",
        indexes = {
                @Index(name = "idx_audit_actor", columnList = "actor_user_id"),
                @Index(name = "idx_audit_time", columnList = "created_at"),
                @Index(name = "idx_audit_target", columnList = "target_type,target_id")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue
    private UUID id;

    // Actor (nullable for system events)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_user_id")
    private User actor;

    // Severity or level (INFO/WARN/ERROR)
    @Enumerated(EnumType.STRING)
    private AuditSeverity severity = AuditSeverity.INFO;

    // Action type, e.g., "USER_LOGIN", "SUBSCRIPTION_CREATED", "REFUND_ISSUED"
    @Column(nullable = false)
    private String actionType;

    // Target object type and id (generic)
    private String targetType;
    private String targetId;

    // Additional contextual payload (JSON)
    @Column(columnDefinition = "jsonb")
    private String payloadJson;

    private String ipAddress;
    private String userAgent;

    private Instant createdAt;

    @PrePersist
    public void prePersist() { createdAt = Instant.now(); }
}
