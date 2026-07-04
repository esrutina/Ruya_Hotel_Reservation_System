package com.ruyahotel.service;

import com.ruyahotel.dao.RoomDAO;
import com.ruyahotel.model.Room;

import java.util.List;

public class RoomService {
    private final RoomDAO roomDAO = new RoomDAO();

    public List<Room> getAllRooms() {
        return roomDAO.getAll();
    }

    public List<Room> getAvailableRooms() {
        return roomDAO.getAvailable();
    }

    public Room getRoom(int roomId) {
        return roomDAO.getById(roomId);
    }

    public boolean addRoom(Room room) {
        if (roomDAO.isRoomNumberTaken(room.getRoomNumber())) return false;
        if (!ValidationService.isPositiveNumber(room.getPricePerNight())) return false;
        if (room.getCapacity() <= 0) return false;
        return roomDAO.add(room);
    }

    public boolean updateRoom(Room room) {
        if (!ValidationService.isPositiveNumber(room.getPricePerNight())) return false;
        if (room.getCapacity() <= 0) return false;
        return roomDAO.update(room);
    }

    public boolean deleteRoom(int roomId) {
        // Use trash system instead of just soft delete
        Room room = getRoom(roomId);
        if (room != null) {
            String itemName = room.getRoomNumber() + " - " + room.getRoomType();
            String originalData = String.format("{\"room_number\":\"%s\",\"room_type\":\"%s\",\"price_per_night\":%.2f,\"capacity\":%d,\"description\":\"%s\",\"features\":%s,\"images\":%s,\"status\":\"%s\"}",
                    room.getRoomNumber(), room.getRoomType(), room.getPricePerNight(), room.getCapacity(),
                    room.getDescription(), roomDAO.toJsonArray(room.getFeatures()), roomDAO.toJsonArray(room.getImages()), room.getStatus());
            
            // Add to trash
            com.ruyahotel.dao.TrashDAO trashDAO = new com.ruyahotel.dao.TrashDAO();
            boolean trashed = trashDAO.add("Room", roomId, itemName, 
                    com.ruyahotel.util.SessionManager.getInstance().getCurrentUser() != null ? 
                    com.ruyahotel.util.SessionManager.getInstance().getCurrentUser().getUserId() : 1,
                    originalData);
            
            if (trashed) {
                // Actually delete the room from rooms table
                return roomDAO.hardDelete(roomId);
            }
        }
        return false;
    }

    public boolean setMaintenance(int roomId) {
        return roomDAO.updateStatus(roomId, "Maintenance");
    }

    public boolean setAvailable(int roomId) {
        return roomDAO.updateStatus(roomId, "Available");
    }
}
