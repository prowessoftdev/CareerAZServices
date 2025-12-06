package com.CareerAZ.repo;


import com.CareerAZ.entity.BillingEventLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BillingEventLogRepository extends JpaRepository<BillingEventLog, UUID> {
    // standard CRUD
}

