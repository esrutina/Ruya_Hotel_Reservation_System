package com.ruyahotel.dao;

import com.ruyahotel.model.Reservation;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    public int create(Reservation r) {
        String sql = "INSERT INTO reservations (user_id, room_id, full_name, email, phone, check_in_date, check_out_date, total_days, total_price, special_request, status, payment_status) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, r.getUserId());
            ps.setInt(2, r.getRoomId());
            ps.setString(3, r.getFullName());
            ps.setString(4, r.getEmail());
            ps.setString(5, r.getPhone());
            ps.setDate(6, Date.valueOf(r.getCheckInDate()));
            ps.setDate(7, Date.valueOf(r.getCheckOutDate()));
            ps.setInt(8, r.getTotalDays());
            ps.setDouble(9, r.getTotalPrice());
            ps.setString(10, r.getSpecialRequest());
            ps.setString(11, r.getStatus());
            ps.setString(12, r.getPaymentStatus());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
            return -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            DBConnection.close(conn, ps);
        }
    }

    public boolean updateStatus(int reservationId, String status) {
        String sql = "UPDATE reservations SET status=? WHERE reservation_id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, reservationId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.close(conn, ps);
        }
    }

    public boolean cancel(int reservationId) {
        return updateStatus(reservationId, "Cancelled");
    }

    public boolean updatePaymentStatus(int reservationId, String paymentStatus) {
        String sql = "UPDATE reservations SET payment_status=? WHERE reservation_id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, paymentStatus);
            ps.setInt(2, reservationId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.close(conn, ps);
        }
    }

    public Reservation getById(int reservationId) {
        String sql = "SELECT r.*, rm.room_type, u.first_name, u.last_name FROM reservations r " +
                "JOIN rooms rm ON r.room_id = rm.room_id JOIN users u ON r.user_id = u.user_id WHERE r.reservation_id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, reservationId);
            rs = ps.executeQuery();
            if (rs.next()) return mapReservation(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return null;
    }

    public List<Reservation> getByUser(int userId) {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT r.*, rm.room_type, u.first_name, u.last_name FROM reservations r " +
                "JOIN rooms rm ON r.room_id = rm.room_id JOIN users u ON r.user_id = u.user_id WHERE r.user_id=? ORDER BY r.created_at DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) list.add(mapReservation(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    public List<Reservation> getAll() {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT r.*, rm.room_type, u.first_name, u.last_name FROM reservations r " +
                "JOIN rooms rm ON r.room_id = rm.room_id JOIN users u ON r.user_id = u.user_id ORDER BY r.created_at DESC";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) list.add(mapReservation(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, st, rs);
        }
        return list;
    }

    public List<Reservation> getByStatus(String status) {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT r.*, rm.room_type, u.first_name, u.last_name FROM reservations r " +
                "JOIN rooms rm ON r.room_id = rm.room_id JOIN users u ON r.user_id = u.user_id WHERE r.status=? ORDER BY r.created_at DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            rs = ps.executeQuery();
            while (rs.next()) list.add(mapReservation(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    public int count() {
        String sql = "SELECT COUNT(*) FROM reservations";
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
        String sql = "SELECT COUNT(*) FROM reservations WHERE status=?";
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

    public double getTotalRevenue() {
        String sql = "SELECT COALESCE(SUM(total_price),0) FROM reservations WHERE status='Checked-Out' OR payment_status='Paid'";
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

    public int getTodayCheckIns() {
        String sql = "SELECT COUNT(*) FROM reservations WHERE check_in_date = CURDATE() AND status IN ('Confirmed','Pending')";
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

    public int getTodayCheckOuts() {
        String sql = "SELECT COUNT(*) FROM reservations WHERE check_out_date = CURDATE() AND status='Checked-In'";
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

    public void expireOldReservations() {
        String sql = "UPDATE reservations SET status='Expired' WHERE status='Pending' AND check_in_date < CURDATE()";
        Connection conn = null;
        Statement st = null;
        try {
            conn = DBConnection.getConnection();
            st = conn.createStatement();
            st.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, st);
        }
    }

    private Reservation mapReservation(ResultSet rs) throws SQLException {
        Reservation r = new Reservation();
        r.setReservationId(rs.getInt("reservation_id"));
        r.setUserId(rs.getInt("user_id"));
        r.setRoomId(rs.getInt("room_id"));
        r.setFullName(rs.getString("full_name"));
        r.setEmail(rs.getString("email"));
        r.setPhone(rs.getString("phone"));
        Date ci = rs.getDate("check_in_date");
        Date co = rs.getDate("check_out_date");
        r.setCheckInDate(ci != null ? ci.toLocalDate() : null);
        r.setCheckOutDate(co != null ? co.toLocalDate() : null);
        r.setTotalDays(rs.getInt("total_days"));
        r.setTotalPrice(rs.getDouble("total_price"));
        r.setSpecialRequest(rs.getString("special_request"));
        r.setStatus(rs.getString("status"));
        r.setPaymentStatus(rs.getString("payment_status"));
        r.setCreatedAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null);
        r.setUpdatedAt(rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null);
        r.setRoomType(rs.getString("room_type"));
        r.setCustomerName(rs.getString("first_name") + " " + rs.getString("last_name"));
        return r;
    }
}
