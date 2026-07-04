package com.ruyahotel.dao;

import com.ruyahotel.model.Setting;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SettingsDAO {

    public String getValue(String key) {
        String sql = "SELECT setting_value FROM system_settings WHERE setting_key=?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, key);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getString("setting_value");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return null;
    }

    public boolean setValue(String key, String value) {
        String sql = "UPDATE system_settings SET setting_value=? WHERE setting_key=?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, value);
            ps.setString(2, key);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.close(conn, ps);
        }
    }

    public List<Setting> getAll() {
        List<Setting> list = new ArrayList<>();
        String sql = "SELECT * FROM system_settings ORDER BY category, setting_key";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                Setting s = new Setting();
                s.setSettingId(rs.getInt("setting_id"));
                s.setSettingKey(rs.getString("setting_key"));
                s.setSettingValue(rs.getString("setting_value"));
                s.setCategory(rs.getString("category"));
                s.setUpdatedAt(rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null);
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, st, rs);
        }
        return list;
    }

    public List<Setting> getByCategory(String category) {
        List<Setting> list = new ArrayList<>();
        String sql = "SELECT * FROM system_settings WHERE category=? ORDER BY setting_key";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, category);
            rs = ps.executeQuery();
            while (rs.next()) {
                Setting s = new Setting();
                s.setSettingId(rs.getInt("setting_id"));
                s.setSettingKey(rs.getString("setting_key"));
                s.setSettingValue(rs.getString("setting_value"));
                s.setCategory(rs.getString("category"));
                s.setUpdatedAt(rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null);
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }
}
