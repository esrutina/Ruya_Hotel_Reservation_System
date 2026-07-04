package com.ruyahotel.dao;

import com.ruyahotel.model.Payment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    public boolean create(Payment p) {
        String sql = "INSERT INTO payments (reservation_id, user_id, amount, payment_method, status, transaction_ref) VALUES (?,?,?,?,?,?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet generatedKeys = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, p.getReservationId());
            ps.setInt(2, p.getUserId());
            ps.setDouble(3, p.getAmount());
            ps.setString(4, p.getPaymentMethod());
            ps.setString(5, p.getStatus());
            ps.setString(6, p.getTransactionRef());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    p.setPaymentId(generatedKeys.getInt(1));
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (generatedKeys != null) try { generatedKeys.close(); } catch (SQLException ignored) {}
            DBConnection.close(conn, ps);
        }
    }

    public boolean markPaid(int paymentId) {
        String sql = "UPDATE payments SET status='Paid', paid_at=NOW() WHERE payment_id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, paymentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.close(conn, ps);
        }
    }

    public boolean updateTransactionRef(int paymentId, String txRef) {
        String sql = "UPDATE payments SET transaction_ref=? WHERE payment_id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, txRef);
            ps.setInt(2, paymentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.close(conn, ps);
        }
    }

    public Payment getById(int paymentId) {
        String sql = "SELECT p.*, u.first_name, u.last_name, r.room_type FROM payments p " +
                "JOIN users u ON p.user_id = u.user_id " +
                "JOIN reservations res ON p.reservation_id = res.reservation_id " +
                "JOIN rooms r ON res.room_id = r.room_id WHERE p.payment_id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, paymentId);
            rs = ps.executeQuery();
            if (rs.next()) return mapPayment(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return null;
    }

    public Payment getByReservation(int reservationId) {
        String sql = "SELECT p.*, u.first_name, u.last_name, r.room_type FROM payments p " +
                "JOIN users u ON p.user_id = u.user_id " +
                "JOIN reservations res ON p.reservation_id = res.reservation_id " +
                "JOIN rooms r ON res.room_id = r.room_id WHERE p.reservation_id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, reservationId);
            rs = ps.executeQuery();
            if (rs.next()) return mapPayment(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return null;
    }

    public List<Payment> getAll() {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT p.*, u.first_name, u.last_name, r.room_type FROM payments p " +
                "JOIN users u ON p.user_id = u.user_id " +
                "JOIN reservations res ON p.reservation_id = res.reservation_id " +
                "JOIN rooms r ON res.room_id = r.room_id ORDER BY p.created_at DESC";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) list.add(mapPayment(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, st, rs);
        }
        return list;
    }

    public List<Payment> getByUser(int userId) {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT p.*, u.first_name, u.last_name, r.room_type FROM payments p " +
                "JOIN users u ON p.user_id = u.user_id " +
                "JOIN reservations res ON p.reservation_id = res.reservation_id " +
                "JOIN rooms r ON res.room_id = r.room_id WHERE p.user_id=? ORDER BY p.created_at DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) list.add(mapPayment(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    private Payment mapPayment(ResultSet rs) throws SQLException {
        Payment p = new Payment();
        p.setPaymentId(rs.getInt("payment_id"));
        p.setReservationId(rs.getInt("reservation_id"));
        p.setUserId(rs.getInt("user_id"));
        p.setAmount(rs.getDouble("amount"));
        p.setPaymentMethod(rs.getString("payment_method"));
        p.setStatus(rs.getString("status"));
        p.setTransactionRef(rs.getString("transaction_ref"));
        p.setPaidAt(rs.getTimestamp("paid_at") != null ? rs.getTimestamp("paid_at").toLocalDateTime() : null);
        p.setCreatedAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null);
        p.setCustomerName(rs.getString("first_name") + " " + rs.getString("last_name"));
        p.setRoomType(rs.getString("room_type"));
        return p;
    }
}
