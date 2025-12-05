package com.CareerAZ.demo.entity;



import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "organizations",
        indexes = {
                @Index(name = "idx_org_slug", columnList = "slug"),
                @Index(name = "idx_org_owner", columnList = "owner_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_org_slug", columnNames = {"slug"})
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String slug;

    // Owner / billing contact - optional link to user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    // Billing email/contact (fallback)
    private String billingEmail;
    private String billingPhone;

    // Plan assigned to org (optional)
    @Enumerated(EnumType.STRING)
    private Plan plan = Plan.FREE;

    // Additional metadata (address, legal info) stored as JSON
    @Column(columnDefinition = "json")
    private String metadataJson;

    @Column(nullable = false)
    private boolean active = true;

    private Instant createdAt;
    private Instant updatedAt;

    @PrePersist
    public void prePersist() { createdAt = Instant.now(); }

    @PreUpdate
    public void preUpdate() { updatedAt = Instant.now(); }
}

