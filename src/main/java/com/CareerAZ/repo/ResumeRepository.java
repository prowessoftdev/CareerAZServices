package com.CareerAZ.repo;


import com.CareerAZ.entity.Resume;
import com.CareerAZ.entity.ResumeStatus;
import com.CareerAZ.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, UUID> {
    List<Resume> findByOwner(User owner);
    List<Resume> findByOwnerId(Long ownerId);
    List<Resume> findByStatus(ResumeStatus status);
    // find latest by owner ordered by version or createdAt (use pageable in service)
    List<Resume> findByOwnerIdOrderByVersionDesc(Long ownerId);
}

