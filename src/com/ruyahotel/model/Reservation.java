package com.ruyahotel.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Reservation {
    private int reservationId;
    private int userId;
    private int roomId;
    private String fullName;
    private String email;
    private String phone;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int totalDays;
    private double totalPrice;
    private String specialRequest;
    private String status;
    private String paymentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // For display
    private String roomType;
    private String customerName;

    public Reservation() {}

    public int getReservationId() { return reservationId; }
    public void setReservationId(int reservationId) { this.reservationId = reservationId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public LocalDate getCheckInDate() { return checkInDate; }
    public void setCheckInDate(LocalDate checkInDate) { this.checkInDate = checkInDate; }

    public LocalDate getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(LocalDate checkOutDate) { this.checkOutDate = checkOutDate; }

    public int getTotalDays() { return totalDays; }
    public void setTotalDays(int totalDays) { this.totalDays = totalDays; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getSpecialRequest() { return specialRequest; }
    public void setSpecialRequest(String specialRequest) { this.specialRequest = specialRequest; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public void calculateTotals(double pricePerNight) {
        if (checkInDate != null && checkOutDate != null) {
            this.totalDays = (int) ChronoUnit.DAYS.between(checkInDate, checkOutDate);
            this.totalPrice = this.totalDays * pricePerNight;
        }
    }

    public boolean canCancel() {
        if (checkInDate == null) return false;
        return !status.equals("Checked-In") && !status.equals("Checked-Out")
                && !status.equals("Cancelled") && !status.equals("Expired")
                && checkInDate.isAfter(LocalDate.now());
    }
}
