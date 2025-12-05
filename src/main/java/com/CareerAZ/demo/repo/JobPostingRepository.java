package com.CareerAZ.demo.repo;


import com.CareerAZ.demo.entity.JobPosting;
import com.CareerAZ.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, UUID> {
    List<JobPosting> findByOwner(User owner);
    List<JobPosting> findByOwnerId(Long ownerId);
    Optional<JobPosting> findByContentHash(String contentHash);

    // search by source URL if saving links
    Optional<JobPosting> findBySourceUrl(String sourceUrl);
}

