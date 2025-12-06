package com.careeraz.services.repo;


import com.careeraz.services.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, UUID> {

    Optional<Organization> findBySlug(String slug);

    List<Organization> findByOwnerId(Long ownerId);

    List<Organization> findByPlan(Plan plan);

    boolean existsBySlug(String slug);
}

