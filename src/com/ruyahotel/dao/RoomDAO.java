package com.ruyahotel.dao;

import com.ruyahotel.model.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    public boolean add(Room room) {
        String sql = "INSERT INTO rooms (room_number, room_type, price_per_night, capacity, description, features, images, status) VALUES (?,?,?,?,?,?,?,?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, room.getRoomNumber());
            ps.setString(2, room.getRoomType());
            ps.setDouble(3, room.getPricePerNight());
            ps.setInt(4, room.getCapacity());
            ps.setString(5, room.getDescription());
            ps.setString(6, toJsonArray(room.getFeatures()));
            ps.setString(7, toJsonArray(room.getImages()));
            ps.setString(8, room.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.close(conn, ps);
        }
    }

    public boolean update(Room room) {
        String sql = "UPDATE rooms SET room_type=?, price_per_night=?, capacity=?, description=?, features=?, images=?, status=? WHERE room_id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, room.getRoomType());
            ps.setDouble(2, room.getPricePerNight());
            ps.setInt(3, room.getCapacity());
            ps.setString(4, room.getDescription());
            ps.setString(5, toJsonArray(room.getFeatures()));
            ps.setString(6, toJsonArray(room.getImages()));
            ps.setString(7, room.getStatus());
            ps.setInt(8, room.getRoomId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.close(conn, ps);
        }
    }

    public boolean updateStatus(int roomId, String status) {
        String sql = "UPDATE rooms SET status=? WHERE room_id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, roomId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.close(conn, ps);
        }
    }

    public boolean softDelete(int roomId) {
        // Instead of just setting status to Deleted, add to trash log
        Room room = getById(roomId);
        if (room != null) {
            String itemName = room.getRoomNumber() + " - " + room.getRoomType();
            String originalData = String.format("{\"room_number\":\"%s\",\"room_type\":\"%s\",\"price_per_night\":%.2f,\"capacity\":%d,\"description\":\"%s\",\"features\":%s,\"images\":%s,\"status\":\"%s\"}",
                    room.getRoomNumber(), room.getRoomType(), room.getPricePerNight(), room.getCapacity(),
                    room.getDescription(), toJsonArray(room.getFeatures()), toJsonArray(room.getImages()), room.getStatus());
            
            // Add to trash
            com.ruyahotel.dao.TrashDAO trashDAO = new com.ruyahotel.dao.TrashDAO();
            boolean trashed = trashDAO.add("Room", roomId, itemName, 
                    com.ruyahotel.util.SessionManager.getInstance().getCurrentUser() != null ? 
                    com.ruyahotel.util.SessionManager.getInstance().getCurrentUser().getUserId() : 1,
                    originalData);
            
            if (trashed) {
                // Actually delete the room
                String sql = "DELETE FROM rooms WHERE room_id=?";
                Connection conn = null;
                PreparedStatement ps = null;
                try {
                    conn = DBConnection.getConnection();
                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, roomId);
                    return ps.executeUpdate() > 0;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                } finally {
                    DBConnection.close(conn, ps);
                }
            }
        }
        return false;
    }

    public boolean hardDelete(int roomId) {
        String sql = "DELETE FROM rooms WHERE room_id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, roomId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.close(conn, ps);
        }
    }

    public Room getById(int roomId) {
        String sql = "SELECT * FROM rooms WHERE room_id=? AND status != 'Deleted'";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, roomId);
            rs = ps.executeQuery();
            if (rs.next()) return mapRoom(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return null;
    }

    public List<Room> getAll() {
        return getByStatus(null);
    }

    public List<Room> getAvailable() {
        return getByStatus("Available");
    }

    private List<Room> getByStatus(String status) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE status != 'Deleted'" + (status != null ? " AND status=?" : "");
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            if (status != null) ps.setString(1, status);
            rs = ps.executeQuery();
            while (rs.next()) rooms.add(mapRoom(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return rooms;
    }

    public List<Room> getByType(String roomType) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE room_type=? AND status != 'Deleted'";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, roomType);
            rs = ps.executeQuery();
            while (rs.next()) rooms.add(mapRoom(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return rooms;
    }

    public boolean isRoomNumberTaken(String roomNumber) {
        String sql = "SELECT 1 FROM rooms WHERE room_number=? AND status != 'Deleted'";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, roomNumber);
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        } finally {
            DBConnection.close(conn, ps, rs);
        }
    }

    public int count() {
        String sql = "SELECT COUNT(*) FROM rooms WHERE status != 'Deleted'";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, st, rs);
        }
        return 0;
    }

    public int countByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM rooms WHERE status=?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return 0;
    }

    private Room mapRoom(ResultSet rs) throws SQLException {
        Room r = new Room();
        r.setRoomId(rs.getInt("room_id"));
        r.setRoomNumber(rs.getString("room_number"));
        r.setRoomType(rs.getString("room_type"));
        r.setPricePerNight(rs.getDouble("price_per_night"));
        r.setCapacity(rs.getInt("capacity"));
        r.setDescription(rs.getString("description"));
        String feat = rs.getString("features");
        if (feat != null) {
            r.setFeatures(parseJsonArray(feat));
        }
        String imgs = rs.getString("images");
        if (imgs != null) {
            r.setImages(parseJsonArray(imgs));
        }
        r.setStatus(rs.getString("status"));
        r.setCreatedAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null);
        r.setUpdatedAt(rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null);
        return r;
    }

    public String toJsonArray(List<String> list) {
        if (list == null) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            sb.append("\"").append(list.get(i).replace("\\", "\\\\").replace("\"", "\\\"")).append("\"");
            if (i < list.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    public List<String> parseJsonArray(String json) {
        List<String> list = new ArrayList<>();
        if (json == null || json.length() <= 2) return list;
        String trimmed = json.substring(1, json.length() - 1);
        if (trimmed.isEmpty()) return list;
        // Simple split for basic JSON arrays without nested objects
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        for (char c : trimmed.toCharArray()) {
            if (c == '"' && (current.length() == 0 || current.charAt(current.length() - 1) != '\\')) {
                inQuotes = !inQuotes;
                if (!inQuotes && current.length() > 0) {
                    list.add(current.toString());
                    current = new StringBuilder();
                }
            } else if (inQuotes) {
                current.append(c);
            }
        }
        return list;
    }
}