package com.ruyahotel.dao;

import com.ruyahotel.model.User;
import com.ruyahotel.util.PasswordHasher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public boolean register(User user) {
        String sql = "INSERT INTO users (first_name, last_name, username, email, phone, password_hash, gender, nationality, role_id, account_status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getUsername());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getPhone());
            ps.setString(6, PasswordHasher.hash(user.getPasswordHash()));
            ps.setString(7, user.getGender());
            ps.setString(8, user.getNationality());
            ps.setInt(9, user.getRoleId());
            ps.setString(10, user.getAccountStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.close(conn, ps);
        }
    }

    public User login(String usernameOrEmail, String password) {
        String sql = "SELECT u.*, r.role_name FROM users u LEFT JOIN roles r ON u.role_id = r.role_id " +
                "WHERE (u.username = ? OR u.email = ?) AND u.account_status = 'Active'";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, usernameOrEmail);
            ps.setString(2, usernameOrEmail);
            rs = ps.executeQuery();
            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                if (PasswordHasher.verify(password, storedHash)) {
                    User user = mapUser(rs);
                    updateLastLogin(user.getUserId());
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return null;
    }

    public User getById(int userId) {
        String sql = "SELECT u.*, r.role_name FROM users u LEFT JOIN roles r ON u.role_id = r.role_id WHERE u.user_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            if (rs.next()) return mapUser(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return null;
    }

    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.*, r.role_name FROM users u LEFT JOIN roles r ON u.role_id = r.role_id WHERE u.account_status != 'Deleted'";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) users.add(mapUser(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, st, rs);
        }
        return users;
    }

    public boolean update(User user) {
        String sql = "UPDATE users SET first_name=?, last_name=?, email=?, phone=?, gender=?, nationality=?, address=?, profile_picture=? WHERE user_id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getGender());
            ps.setString(6, user.getNationality());
            ps.setString(7, user.getAddress());
            ps.setString(8, user.getProfilePicture());
            ps.setInt(9, user.getUserId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.close(conn, ps);
        }
    }

    public boolean updatePassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password_hash=? WHERE user_id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, PasswordHasher.hash(newPassword));
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.close(conn, ps);
        }
    }

    public boolean softDelete(int userId) {
        return updateStatus(userId, "Deleted");
    }

    public boolean restoreFromTrash(int userId) {
        return updateStatus(userId, "Active");
    }

    public boolean updateStatus(int userId, String status) {
        String sql = "UPDATE users SET account_status=? WHERE user_id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.close(conn, ps);
        }
    }

    public boolean updateRole(int userId, int roleId) {
        String sql = "UPDATE users SET role_id=? WHERE user_id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, roleId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.close(conn, ps);
        }
    }

    public boolean isUsernameTaken(String username) {
        return exists("username", username);
    }

    public boolean isEmailTaken(String email) {
        return exists("email", email);
    }

    private boolean exists(String field, String value) {
        String sql = "SELECT 1 FROM users WHERE " + field + " = ? AND account_status != 'Deleted'";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, value);
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        } finally {
            DBConnection.close(conn, ps, rs);
        }
    }

    private void updateLastLogin(int userId) {
        String sql = "UPDATE users SET last_login=NOW() WHERE user_id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException ignored) {} finally {
            DBConnection.close(conn, ps);
        }
    }

    private User mapUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setUserId(rs.getInt("user_id"));
        u.setFirstName(rs.getString("first_name"));
        u.setLastName(rs.getString("last_name"));
        u.setUsername(rs.getString("username"));
        u.setEmail(rs.getString("email"));
        u.setPhone(rs.getString("phone"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setGender(rs.getString("gender"));
        u.setNationality(rs.getString("nationality"));
        u.setAddress(rs.getString("address"));
        u.setProfilePicture(rs.getString("profile_picture"));
        u.setRoleId(rs.getInt("role_id"));
        u.setAccountStatus(rs.getString("account_status"));
        u.setRegistrationDate(rs.getTimestamp("registration_date") != null ? rs.getTimestamp("registration_date").toLocalDateTime() : null);
        u.setLastLogin(rs.getTimestamp("last_login") != null ? rs.getTimestamp("last_login").toLocalDateTime() : null);
        u.setRoleName(rs.getString("role_name"));
        return u;
    }
}
