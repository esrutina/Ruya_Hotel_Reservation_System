package com.ruyahotel.model;

import java.time.LocalDateTime;

public class Payment {
    private int paymentId;
    private int reservationId;
    private int userId;
    private double amount;
    private String paymentMethod;
    private String status;
    private String transactionRef;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;

    // For display
    private String customerName;
    private String roomType;

    public Payment() {}

    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }

    public int getReservationId() { return reservationId; }
    public void setReservationId(int reservationId) { this.reservationId = reservationId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTransactionRef() { return transactionRef; }
    public void setTransactionRef(String transactionRef) { this.transactionRef = transactionRef; }

    public LocalDateTime getPaidAt() { return paidAt; }
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }

    public boolean isPaid() {
        return "Paid".equals(status);
    }
}
