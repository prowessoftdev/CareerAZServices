package com.careeraz.services.repo;


import com.careeraz.services.entity.CoverLetter;
import com.careeraz.services.entity.JobPosting;
import com.careeraz.services.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CoverLetterRepository extends JpaRepository<CoverLetter, UUID> {
    List<CoverLetter> findByUser(User user);

    List<CoverLetter> findByUserId(Long userId);

    List<CoverLetter> findByJobPosting(JobPosting jobPosting);

    List<CoverLetter> findByJobPostingId(java.util.UUID jobPostingId);
}

