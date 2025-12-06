package com.careeraz.services.repo;



import com.careeraz.services.entity.AuditLog;
import com.careeraz.services.entity.AuditSeverity;
import com.careeraz.services.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {

    List<AuditLog> findByActor(User actor);

    List<AuditLog> findByActorId(Long actorId);

    List<AuditLog> findBySeverity(AuditSeverity severity);

    List<AuditLog> findByTargetTypeAndTargetId(String targetType, String targetId);

    List<AuditLog> findTop50ByOrderByCreatedAtDesc();
}

