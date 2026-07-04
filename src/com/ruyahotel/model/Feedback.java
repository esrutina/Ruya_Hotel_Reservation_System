package com.ruyahotel.model;

import java.time.LocalDateTime;

public class Feedback {
    private int feedbackId;
    private int reservationId;
    private int userId;
    private int roomId;
    private int rating;
    private String comment;
    private String status;
    private LocalDateTime createdAt;

    // For display
    private String customerName;
    private String roomType;

    public Feedback() {}

    public int getFeedbackId() { return feedbackId; }
    public void setFeedbackId(int feedbackId) { this.feedbackId = feedbackId; }

    public int getReservationId() { return reservationId; }
    public void setReservationId(int reservationId) { this.reservationId = reservationId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }

    public String getStarDisplay() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rating; i++) sb.append("\u2605");
        for (int i = rating; i < 5; i++) sb.append("\u2606");
        return sb.toString();
    }
}
