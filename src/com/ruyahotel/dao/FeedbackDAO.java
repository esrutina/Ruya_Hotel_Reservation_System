package com.ruyahotel.dao;

import com.ruyahotel.model.Feedback;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeedbackDAO {

    public boolean create(Feedback f) {
        String sql = "INSERT INTO feedback (reservation_id, user_id, room_id, rating, comment) VALUES (?,?,?,?,?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, f.getReservationId());
            ps.setInt(2, f.getUserId());
            ps.setInt(3, f.getRoomId());
            ps.setInt(4, f.getRating());
            ps.setString(5, f.getComment());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.close(conn, ps);
        }
    }

    public boolean softDelete(int feedbackId) {
        String sql = "UPDATE feedback SET status='Deleted' WHERE feedback_id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, feedbackId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.close(conn, ps);
        }
    }

    public Feedback getById(int feedbackId) {
        String sql = "SELECT f.*, u.first_name, u.last_name, r.room_type FROM feedback f " +
                "JOIN users u ON f.user_id = u.user_id JOIN rooms r ON f.room_id = r.room_id WHERE f.feedback_id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, feedbackId);
            rs = ps.executeQuery();
            if (rs.next()) return mapFeedback(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return null;
    }

    public List<Feedback> getAll() {
        List<Feedback> list = new ArrayList<>();
        String sql = "SELECT f.*, u.first_name, u.last_name, r.room_type FROM feedback f " +
                "JOIN users u ON f.user_id = u.user_id JOIN rooms r ON f.room_id = r.room_id WHERE f.status='Active' ORDER BY f.created_at DESC";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) list.add(mapFeedback(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, st, rs);
        }
        return list;
    }

    public List<Feedback> getByUser(int userId) {
        List<Feedback> list = new ArrayList<>();
        String sql = "SELECT f.*, u.first_name, u.last_name, r.room_type FROM feedback f " +
                "JOIN users u ON f.user_id = u.user_id JOIN rooms r ON f.room_id = r.room_id WHERE f.user_id=? AND f.status='Active' ORDER BY f.created_at DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) list.add(mapFeedback(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    public boolean hasFeedbackForReservation(int reservationId) {
        String sql = "SELECT 1 FROM feedback WHERE reservation_id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, reservationId);
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        } finally {
            DBConnection.close(conn, ps, rs);
        }
    }

    public double getAverageRating() {
        String sql = "SELECT COALESCE(AVG(rating),0) FROM feedback WHERE status='Active'";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, st, rs);
        }
        return 0;
    }

    private Feedback mapFeedback(ResultSet rs) throws SQLException {
        Feedback f = new Feedback();
        f.setFeedbackId(rs.getInt("feedback_id"));
        f.setReservationId(rs.getInt("reservation_id"));
        f.setUserId(rs.getInt("user_id"));
        f.setRoomId(rs.getInt("room_id"));
        f.setRating(rs.getInt("rating"));
        f.setComment(rs.getString("comment"));
        f.setStatus(rs.getString("status"));
        f.setCreatedAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null);
        f.setCustomerName(rs.getString("first_name") + " " + rs.getString("last_name"));
        f.setRoomType(rs.getString("room_type"));
        return f;
    }
}
