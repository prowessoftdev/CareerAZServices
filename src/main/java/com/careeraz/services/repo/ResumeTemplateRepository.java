package com.careeraz.services.repo;


import com.careeraz.services.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResumeTemplateRepository extends JpaRepository<ResumeTemplate, Long> {
    Optional<ResumeTemplate> findBySlug(String slug);

    boolean existsBySlug(String slug);
}

