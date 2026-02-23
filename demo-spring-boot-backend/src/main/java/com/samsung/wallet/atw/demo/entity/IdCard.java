package com.samsung.wallet.atw.demo.entity;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Digital ID card for Samsung Wallet.
 * Spec: https://developer.samsung.com/wallet/addtosamsungwallet/walletcards/digitalids.html
 */
@Entity
@Table(name = "id_cards")
public class IdCard {

    @Id
    @Column(name = "ref_id", length = 128)
    private String refId;

    @Column(nullable = false, length = 32)
    private String title;

    @Column(name = "holder_name", nullable = false, length = 64)
    private String holderName;

    @Column(name = "second_holder_name", length = 64)
    private String secondHolderName;

    @Column(nullable = false, length = 64)
    private String identifier;

    @Column(name = "id_number", length = 64)
    private String idNumber;

    @Column(name = "provider_name", nullable = false, length = 32)
    private String providerName;

    @Column(name = "organization", length = 64)
    private String organization;

    @Column(name = "position", length = 64)
    private String position;

    @Column(name = "expiry_at")
    private Instant expiryAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    public String getRefId() { return refId; }
    public void setRefId(String refId) { this.refId = refId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getHolderName() { return holderName; }
    public void setHolderName(String holderName) { this.holderName = holderName; }
    public String getSecondHolderName() { return secondHolderName; }
    public void setSecondHolderName(String secondHolderName) { this.secondHolderName = secondHolderName; }
    public String getIdentifier() { return identifier; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }
    public String getIdNumber() { return idNumber; }
    public void setIdNumber(String idNumber) { this.idNumber = idNumber; }
    public String getProviderName() { return providerName; }
    public void setProviderName(String providerName) { this.providerName = providerName; }
    public String getOrganization() { return organization; }
    public void setOrganization(String organization) { this.organization = organization; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public Instant getExpiryAt() { return expiryAt; }
    public void setExpiryAt(Instant expiryAt) { this.expiryAt = expiryAt; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
