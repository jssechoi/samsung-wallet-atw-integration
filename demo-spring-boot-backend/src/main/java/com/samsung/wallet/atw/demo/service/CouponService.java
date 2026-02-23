package com.samsung.wallet.atw.demo.service;

import com.samsung.wallet.atw.demo.entity.Coupon;
import com.samsung.wallet.atw.demo.repository.CouponRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponService {

    private final CouponRepository repository;

    public CouponService(CouponRepository repository) {
        this.repository = repository;
    }

    public List<Coupon> findAll() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    public Coupon findByRefId(String refId) {
        return repository.findById(refId).orElse(null);
    }
}
