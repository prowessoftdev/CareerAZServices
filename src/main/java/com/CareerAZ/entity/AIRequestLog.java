package com.CareerAZ.entity;



import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "ai_request_logs",
        indexes = {
                @Index(name = "idx_ai_user_time", columnList = "user_id, created_at")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIRequestLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private AIRequestFeature feature; // BULLET_GEN, ATS_ANALYSIS, COVER_LETTER, SUMMARY

    private String modelName; // Claude_Sonnet_4, GPT-4, etc.

    @Column(length = 128)
    private String promptHash;

    @Column(length = 512)
    private String promptSnippet;

    private Integer tokensIn;
    private Integer tokensOut;

    @Column(precision = 10, scale = 4)
    private BigDecimal cost;

    // truncated/short response JSON or pointer to S3
    @Column(columnDefinition = "json")
    private String responseJson;

    private Instant createdAt;

    @PrePersist
    public void prePersist() { createdAt = Instant.now(); }
}

