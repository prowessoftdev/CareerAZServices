package com.CareerAZ.demo.repo;


import com.CareerAZ.demo.entity.Coupon;
import com.CareerAZ.demo.entity.CouponType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.time.Instant;
import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, UUID> {

    Optional<Coupon> findByCode(String code);

    boolean existsByCode(String code);

    List<Coupon> findByActiveTrue();

    List<Coupon> findByType(CouponType type);

    List<Coupon> findByValidFromBeforeAndValidUntilAfter(Instant now1, Instant now2);
}

