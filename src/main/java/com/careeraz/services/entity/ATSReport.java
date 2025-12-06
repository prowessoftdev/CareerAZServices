package com.careeraz.services.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "ats_reports",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_ats_resume_job", columnNames = {"resume_id", "job_posting_id"})
        },
        indexes = {
                @Index(name = "idx_ats_resume", columnList = "resume_id"),
                @Index(name = "idx_ats_jobposting", columnList = "job_posting_id")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ATSReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_posting_id", nullable = false)
    private JobPosting jobPosting;

    // 0-100
    private Integer score;

    // component scores: keywordMatch, skillsMatch, formatting, experience...
    @Column(columnDefinition = "json")
    private String componentScoresJson;

    // gaps: missing keywords, missing skills, suggestions
    @Column(columnDefinition = "json")
    private String gapsJson;

    @Column(columnDefinition = "json")
    private String suggestionsJson;

    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
    }
}

