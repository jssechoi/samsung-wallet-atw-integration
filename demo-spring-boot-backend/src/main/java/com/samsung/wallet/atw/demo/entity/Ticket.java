package com.samsung.wallet.atw.demo.entity;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Ticket (event pass) entity. refId is the primary key and is used as Samsung Wallet refId.
 */
@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @Column(name = "ref_id", length = 128)
    private String refId;

    @Column(nullable = false, length = 32)
    @Enumerated(EnumType.STRING)
    private TicketStatus status = TicketStatus.ISSUED;

    @Column(length = 256)
    private String title;

    @Column(length = 256)
    private String venue;

    @Column(length = 64)
    private String gate;

    @Column(name = "reservation_number", length = 32)
    private String reservationNumber;

    @Column(name = "provider_name", length = 32)
    private String providerName;

    @Column(name = "event_at")
    private Instant eventAt;

    @Column(name = "grouping_id", length = 32)
    private String groupingId;

    @Column(name = "order_id", length = 32)
    private String orderId;

    @Column(name = "classification", length = 16)
    private String classification;

    @Column(name = "holder_name", length = 64)
    private String holderName;

    @Column(name = "grade", length = 32)
    private String grade;

    @Column(name = "barcode_value", length = 256)
    private String barcodeValue;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    public enum TicketStatus {
        ISSUED,
        USED,
        CANCELLED,
        EXPIRED
    }

    public String getRefId() { return refId; }
    public void setRefId(String refId) { this.refId = refId; }
    public TicketStatus getStatus() { return status; }
    public void setStatus(TicketStatus status) { this.status = status; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }
    public String getGate() { return gate; }
    public void setGate(String gate) { this.gate = gate; }
    public String getReservationNumber() { return reservationNumber; }
    public void setReservationNumber(String reservationNumber) { this.reservationNumber = reservationNumber; }
    public String getProviderName() { return providerName; }
    public void setProviderName(String providerName) { this.providerName = providerName; }
    public Instant getEventAt() { return eventAt; }
    public void setEventAt(Instant eventAt) { this.eventAt = eventAt; }
    public String getGroupingId() { return groupingId; }
    public void setGroupingId(String groupingId) { this.groupingId = groupingId; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getClassification() { return classification; }
    public void setClassification(String classification) { this.classification = classification; }
    public String getHolderName() { return holderName; }
    public void setHolderName(String holderName) { this.holderName = holderName; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    public String getBarcodeValue() { return barcodeValue; }
    public void setBarcodeValue(String barcodeValue) { this.barcodeValue = barcodeValue; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
