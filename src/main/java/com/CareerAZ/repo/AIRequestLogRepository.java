package com.CareerAZ.repo;


import com.CareerAZ.entity.AIRequestFeature;
import com.CareerAZ.entity.AIRequestLog;
import com.CareerAZ.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AIRequestLogRepository extends JpaRepository<AIRequestLog, UUID> {
    List<AIRequestLog> findByUser(User user);
    List<AIRequestLog> findByUserId(Long userId);
    List<AIRequestLog> findByFeature(AIRequestFeature feature);

    // for billing: find by user and between dates -- use method name with Date params in service if needed
    List<AIRequestLog> findByPromptHash(String promptHash);

    // helpful for quick latest usage
    List<AIRequestLog> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);
}

