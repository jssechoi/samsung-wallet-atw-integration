package com.samsung.wallet.atw.demo.repository;

import com.samsung.wallet.atw.demo.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, String> {

    List<Coupon> findAllByOrderByCreatedAtDesc();
}
