package com.CareerAZ.demo.repo;


import com.CareerAZ.demo.entity.ATSReport;
import com.CareerAZ.demo.entity.JobPosting;
import com.CareerAZ.demo.entity.Resume;
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

