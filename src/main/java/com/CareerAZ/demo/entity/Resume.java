package com.CareerAZ.demo.entity;



import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "resumes", indexes = {
        @Index(name = "idx_resumes_user", columnList = "user_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @Column(nullable = false)
    private String title; // "Resume - SWE"

    private Integer version = 1;

    // structured resume JSON (sections). Use json in Postgres.
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "response_json", columnDefinition = "JSON")
    private String dataJson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    private ResumeTemplate template;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResumeStatus status = ResumeStatus.DRAFT;

    // S3 keys or URLs for exported files
    private String s3KeyPdf;
    private String s3KeyDocx;

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

