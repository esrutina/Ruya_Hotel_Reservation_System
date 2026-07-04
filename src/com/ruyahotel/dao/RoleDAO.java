package com.ruyahotel.dao;

import com.ruyahotel.model.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO {

    public boolean create(Role role) {
        String sql = "INSERT INTO roles (role_name, description, permissions) VALUES (?,?,?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, role.getRoleName());
            ps.setString(2, role.getDescription());
            ps.setString(3, toJsonArray(role.getPermissions()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.close(conn, ps);
        }
    }

    public boolean update(Role role) {
        String sql = "UPDATE roles SET role_name=?, description=?, permissions=?, status=? WHERE role_id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, role.getRoleName());
            ps.setString(2, role.getDescription());
            ps.setString(3, toJsonArray(role.getPermissions()));
            ps.setString(4, role.getStatus());
            ps.setInt(5, role.getRoleId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.close(conn, ps);
        }
    }

    public boolean softDelete(int roleId) {
        String sql = "UPDATE roles SET status='Deleted' WHERE role_id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, roleId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.close(conn, ps);
        }
    }

    public Role getById(int roleId) {
        String sql = "SELECT * FROM roles WHERE role_id=? AND status != 'Deleted'";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, roleId);
            rs = ps.executeQuery();
            if (rs.next()) return mapRole(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return null;
    }

    public List<Role> getAll() {
        List<Role> list = new ArrayList<>();
        String sql = "SELECT * FROM roles WHERE status != 'Deleted'";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) list.add(mapRole(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, st, rs);
        }
        return list;
    }

    public boolean isNameTaken(String roleName) {
        String sql = "SELECT 1 FROM roles WHERE role_name=? AND status != 'Deleted'";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, roleName);
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        } finally {
            DBConnection.close(conn, ps, rs);
        }
    }

    private Role mapRole(ResultSet rs) throws SQLException {
        Role r = new Role();
        r.setRoleId(rs.getInt("role_id"));
        r.setRoleName(rs.getString("role_name"));
        r.setDescription(rs.getString("description"));
        String perms = rs.getString("permissions");
        if (perms != null) {
            r.setPermissions(parseJsonArray(perms));
        }
        r.setStatus(rs.getString("status"));
        r.setCreatedAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null);
        r.setUpdatedAt(rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null);
        return r;
    }

    private String toJsonArray(List<String> list) {
        if (list == null) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            sb.append("\"").append(list.get(i).replace("\\", "\\\\").replace("\"", "\\\"")).append("\"");
            if (i < list.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    private List<String> parseJsonArray(String json) {
        List<String> list = new ArrayList<>();
        if (json == null || json.length() <= 2) return list;
        String trimmed = json.substring(1, json.length() - 1);
        if (trimmed.isEmpty()) return list;
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
