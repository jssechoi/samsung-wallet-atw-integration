package com.samsung.wallet.atw.demo.entity;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Boarding pass (airlines) for Samsung Wallet.
 * Spec: https://developer.samsung.com/wallet/addtosamsungwallet/walletcards/boardingpass.html
 */
@Entity
@Table(name = "boarding_passes")
public class BoardingPass {

    @Id
    @Column(name = "ref_id", length = 128)
    private String refId;

    @Column(nullable = false, length = 32)
    private String title;

    @Column(name = "provider_name", nullable = false, length = 32)
    private String providerName;

    @Column(name = "passenger_name", length = 64)
    private String passengerName;

    @Column(name = "vehicle_number", length = 32)
    private String vehicleNumber;

    @Column(name = "seat_class", length = 32)
    private String seatClass;

    @Column(name = "seat_number", length = 16)
    private String seatNumber;

    @Column(name = "reservation_number", nullable = false, length = 32)
    private String reservationNumber;

    @Column(name = "depart_name", length = 32)
    private String departName;

    @Column(name = "depart_code", length = 8)
    private String departCode;

    @Column(name = "depart_terminal", length = 8)
    private String departTerminal;

    @Column(name = "arrive_name", length = 32)
    private String arriveName;

    @Column(name = "arrive_code", length = 8)
    private String arriveCode;

    @Column(name = "grouping_id", length = 32)
    private String groupingId;

    @Column(name = "depart_gate", length = 8)
    private String departGate;

    @Column(name = "arrive_terminal", length = 8)
    private String arriveTerminal;

    @Column(name = "arrive_gate", length = 8)
    private String arriveGate;

    @Column(name = "baggage_allowance", length = 16)
    private String baggageAllowance;

    @Column(name = "boarding_seq_no", length = 32)
    private String boardingSeqNo;

    @Column(name = "barcode_value", length = 256)
    private String barcodeValue;

    @Column(name = "depart_at")
    private Instant departAt;

    @Column(name = "arrive_at")
    private Instant arriveAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    public String getRefId() { return refId; }
    public void setRefId(String refId) { this.refId = refId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getProviderName() { return providerName; }
    public void setProviderName(String providerName) { this.providerName = providerName; }
    public String getPassengerName() { return passengerName; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }
    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }
    public String getSeatClass() { return seatClass; }
    public void setSeatClass(String seatClass) { this.seatClass = seatClass; }
    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }
    public String getReservationNumber() { return reservationNumber; }
    public void setReservationNumber(String reservationNumber) { this.reservationNumber = reservationNumber; }
    public String getDepartName() { return departName; }
    public void setDepartName(String departName) { this.departName = departName; }
    public String getDepartCode() { return departCode; }
    public void setDepartCode(String departCode) { this.departCode = departCode; }
    public String getDepartTerminal() { return departTerminal; }
    public void setDepartTerminal(String departTerminal) { this.departTerminal = departTerminal; }
    public String getArriveName() { return arriveName; }
    public void setArriveName(String arriveName) { this.arriveName = arriveName; }
    public String getArriveCode() { return arriveCode; }
    public void setArriveCode(String arriveCode) { this.arriveCode = arriveCode; }
    public String getGroupingId() { return groupingId; }
    public void setGroupingId(String groupingId) { this.groupingId = groupingId; }
    public String getDepartGate() { return departGate; }
    public void setDepartGate(String departGate) { this.departGate = departGate; }
    public String getArriveTerminal() { return arriveTerminal; }
    public void setArriveTerminal(String arriveTerminal) { this.arriveTerminal = arriveTerminal; }
    public String getArriveGate() { return arriveGate; }
    public void setArriveGate(String arriveGate) { this.arriveGate = arriveGate; }
    public String getBaggageAllowance() { return baggageAllowance; }
    public void setBaggageAllowance(String baggageAllowance) { this.baggageAllowance = baggageAllowance; }
    public String getBoardingSeqNo() { return boardingSeqNo; }
    public void setBoardingSeqNo(String boardingSeqNo) { this.boardingSeqNo = boardingSeqNo; }
    public String getBarcodeValue() { return barcodeValue; }
    public void setBarcodeValue(String barcodeValue) { this.barcodeValue = barcodeValue; }
    public Instant getDepartAt() { return departAt; }
    public void setDepartAt(Instant departAt) { this.departAt = departAt; }
    public Instant getArriveAt() { return arriveAt; }
    public void setArriveAt(Instant arriveAt) { this.arriveAt = arriveAt; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
