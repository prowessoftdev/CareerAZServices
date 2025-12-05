package com.CareerAZ.demo.entity;



import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "cover_letters")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoverLetter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id")
    private Resume resume;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_posting_id")
    private JobPosting jobPosting;

    @Column(columnDefinition = "text")
    private String content;

    @Enumerated(EnumType.STRING)
    private CoverLetterTone tone = CoverLetterTone.FORMAL;

    @Enumerated(EnumType.STRING)
    private CoverLetterLength length = CoverLetterLength.MEDIUM;

    private String s3KeyPdf;

    private Instant createdAt;
    private Instant updatedAt;

    @PrePersist
    public void prePersist() { createdAt = Instant.now(); }

    @PreUpdate
    public void preUpdate() { updatedAt = Instant.now(); }
}

