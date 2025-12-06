package com.CareerAZ.repo;


import com.CareerAZ.entity.ResumeTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResumeTemplateRepository extends JpaRepository<ResumeTemplate, Long> {
    Optional<ResumeTemplate> findBySlug(String slug);
    boolean existsBySlug(String slug);
}

