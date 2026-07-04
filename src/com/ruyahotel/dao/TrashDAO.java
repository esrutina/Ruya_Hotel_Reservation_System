package com.ruyahotel.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrashDAO {

    public boolean add(String itemType, int itemId, String itemName, int deletedBy, String originalData) {
        String sql = "INSERT INTO trash_log (item_type, item_id, item_name, deleted_by, original_data) VALUES (?,?,?,?,?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, itemType);
            ps.setInt(2, itemId);
            ps.setString(3, itemName);
            ps.setInt(4, deletedBy);
            ps.setString(5, originalData);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.close(conn, ps);
        }
    }

    public boolean remove(int trashId) {
        String sql = "DELETE FROM trash_log WHERE trash_id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, trashId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.close(conn, ps);
        }
    }

    public boolean restore(int trashId) {
        // Instead of updating a restored flag, we'll delete the item from trash
        // since restoration means removing from trash view
        String sql = "DELETE FROM trash_log WHERE trash_id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, trashId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.close(conn, ps);
        }
    }

    public String getOriginalData(int trashId) {
        String sql = "SELECT original_data FROM trash_log WHERE trash_id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, trashId);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getString("original_data");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return null;
    }

    public List<Object[]> getAll() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT t.*, u.username as deleter_name FROM trash_log t LEFT JOIN users u ON t.deleted_by = u.user_id ORDER BY t.deleted_at DESC";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                Object[] row = new Object[7];
                row[0] = rs.getInt("trash_id");
                row[1] = rs.getString("item_type");
                row[2] = rs.getString("item_name");
                row[3] = rs.getTimestamp("deleted_at");
                row[4] = rs.getString("deleter_name");
                row[5] = rs.getInt("item_id");
                row[6] = rs.getString("original_data");
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, st, rs);
        }
        return list;
    }
}
