package com.ruyahotel.service;

import com.ruyahotel.dao.TrashDAO;
import com.ruyahotel.dao.UserDAO;
import com.ruyahotel.model.User;

import java.util.List;

public class UserService {
    private final UserDAO userDAO = new UserDAO();
    private final TrashDAO trashDAO = new TrashDAO();

    public List<User> getAllUsers() {
        return userDAO.getAll();
    }

    public User getUser(int userId) {
        return userDAO.getById(userId);
    }

    public boolean updateProfile(User user) {
        return userDAO.update(user);
    }

    public boolean changePassword(int userId, String current, String newPass, String confirm) {
        if (!newPass.equals(confirm)) return false;
        if (!ValidationService.isStrongPassword(newPass)) return false;
        User u = userDAO.getById(userId);
        if (u == null) return false;
        // Verify current password
        if (!com.ruyahotel.util.PasswordHasher.verify(current, u.getPasswordHash())) return false;
        return userDAO.updatePassword(userId, newPass);
    }

    public boolean adminResetPassword(int userId, String newPass) {
        return userDAO.updatePassword(userId, newPass);
    }

    public boolean changeRole(int userId, int roleId) {
        return userDAO.updateRole(userId, roleId);
    }

    public boolean toggleStatus(int userId) {
        User u = userDAO.getById(userId);
        if (u == null) return false;
        String newStatus = "Active".equals(u.getAccountStatus()) ? "Inactive" : "Active";
        return userDAO.updateStatus(userId, newStatus);
    }

    public boolean deleteUser(int userId, int deletedBy) {
        User u = userDAO.getById(userId);
        if (u == null) return false;
        // Add to trash log
        String jsonData = "{\"username\":\"" + escapeJson(u.getUsername()) + "\","
                + "\"email\":\"" + escapeJson(u.getEmail()) + "\","
                + "\"role_id\":" + u.getRoleId() + "}";
        trashDAO.add("User", userId, u.getFullName(), deletedBy, jsonData);
        return userDAO.softDelete(userId);
    }

    public boolean addUser(User user) {
        if (userDAO.isUsernameTaken(user.getUsername())) return false;
        if (userDAO.isEmailTaken(user.getEmail())) return false;
        user.setAccountStatus("Active");
        return userDAO.register(user);
    }

    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
