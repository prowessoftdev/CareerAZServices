package com.CareerAZ.demo.repo;

import com.CareerAZ.demo.entity.CoverLetter;
import com.CareerAZ.demo.entity.JobPosting;
import com.CareerAZ.demo.entity.User;
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

