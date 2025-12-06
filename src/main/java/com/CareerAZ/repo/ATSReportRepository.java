package com.CareerAZ.repo;


import com.CareerAZ.entity.ATSReport;
import com.CareerAZ.entity.JobPosting;
import com.CareerAZ.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ATSReportRepository extends JpaRepository<ATSReport, UUID> {
    List<ATSReport> findByResume(Resume resume);
    List<ATSReport> findByResumeId(UUID resumeId);
    List<ATSReport> findByJobPosting(JobPosting jobPosting);
    List<ATSReport> findByJobPostingId(UUID jobPostingId);

    Optional<ATSReport> findByResumeIdAndJobPostingId(UUID resumeId, UUID jobPostingId);
}

