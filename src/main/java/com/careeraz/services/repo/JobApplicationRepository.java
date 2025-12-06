package com.careeraz.services.repo;


import com.careeraz.services.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, UUID> {
    List<JobApplication> findByUser(User user);

    List<JobApplication> findByUserId(Long userId);

    List<JobApplication> findByUserIdAndStatus(Long userId, ApplicationStatus status);

    // find recent applications for kanban
    List<JobApplication> findByStatus(ApplicationStatus status);
}

