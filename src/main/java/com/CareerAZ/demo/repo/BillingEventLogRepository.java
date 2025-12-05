package com.CareerAZ.demo.repo;


import com.CareerAZ.demo.entity.BillingEventLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BillingEventLogRepository extends JpaRepository<BillingEventLog, UUID> {
    // standard CRUD
}

