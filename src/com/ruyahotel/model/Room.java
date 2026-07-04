package com.ruyahotel.model;

import java.time.LocalDateTime;
import java.util.List;

public class Room {
    private int roomId;
    private String roomNumber;
    private String roomType;
    private double pricePerNight;
    private int capacity;
    private String description;
    private List<String> features;
    private List<String> images;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Room() {}

    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }

    public double getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(double pricePerNight) { this.pricePerNight = pricePerNight; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getFeatures() { return features; }
    public void setFeatures(List<String> features) { this.features = features; }

    public List<String> getImages() { return images; }
    public void setImages(List<String> images) { this.images = images; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public boolean isAvailable() {
        return "Available".equals(status);
    }
}
