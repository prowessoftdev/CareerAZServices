package com.CareerAZ.entity;



import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "resume_templates",
        uniqueConstraints = {@UniqueConstraint(name = "uq_template_slug", columnNames = {"slug"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(nullable = false, unique = true)
    private String slug; // "ats-optimized", "modern"

    @Column(columnDefinition = "boolean default false")
    private boolean isAtsOptimized = false;

    private String previewS3Key;

    private Instant createdAt;
    private Instant updatedAt;

    @PrePersist
    public void prePersist() { createdAt = Instant.now(); }

    @PreUpdate
    public void preUpdate() { updatedAt = Instant.now(); }
}

