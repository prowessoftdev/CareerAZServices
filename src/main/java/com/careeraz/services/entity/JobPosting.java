package com.careeraz.services.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "job_postings",
        indexes = {@Index(name = "idx_jobposting_hash", columnList = "content_hash")})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPosting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // optional: who saved/analyzed it
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;

    private String sourceUrl;

    // raw text/paste
    @Column(columnDefinition = "text")
    private String rawText;

    // parsed JSON: keywords, skills, yearsExp, education, etc.
    @Column(columnDefinition = "json")
    private String parsedJson;

    // hash for dedupe of job description text
    @Column(length = 64)
    private String contentHash;

    private Instant createdAt;
    private Instant analyzedAt;

    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        analyzedAt = Instant.now();
    }
}

