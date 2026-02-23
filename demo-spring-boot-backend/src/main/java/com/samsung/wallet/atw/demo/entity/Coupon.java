package com.samsung.wallet.atw.demo.entity;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Coupon for Samsung Wallet.
 * Spec: https://developer.samsung.com/wallet/addtosamsungwallet/walletcards/coupon.html
 */
@Entity
@Table(name = "coupons")
public class Coupon {

    @Id
    @Column(name = "ref_id", length = 128)
    private String refId;

    @Column(nullable = false, length = 32)
    private String title;

    @Column(name = "brand_name", length = 32)
    private String brandName;

    @Column(name = "expiry_at", nullable = false)
    private Instant expiryAt;

    @Column(name = "barcode_value", length = 256)
    private String barcodeValue;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    public String getRefId() { return refId; }
    public void setRefId(String refId) { this.refId = refId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }
    public Instant getExpiryAt() { return expiryAt; }
    public void setExpiryAt(Instant expiryAt) { this.expiryAt = expiryAt; }
    public String getBarcodeValue() { return barcodeValue; }
    public void setBarcodeValue(String barcodeValue) { this.barcodeValue = barcodeValue; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
