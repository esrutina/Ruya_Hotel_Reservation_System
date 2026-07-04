package com.ruyahotel.service;

import com.ruyahotel.dao.UserDAO;
import com.ruyahotel.model.User;
import com.ruyahotel.util.SessionManager;

public class AuthService {
    private final UserDAO userDAO = new UserDAO();

    public String register(User user) {
        String error = ValidationService.validateRegistration(
                user.getFirstName(), user.getLastName(), user.getUsername(),
                user.getEmail(), user.getPhone(), user.getPasswordHash(), user.getPasswordHash()
        );
        if (error != null) return error;

        if (userDAO.isUsernameTaken(user.getUsername())) return "Username already taken";
        if (userDAO.isEmailTaken(user.getEmail())) return "Email already registered";

        user.setRoleId(4); // Customer
        user.setAccountStatus("Active");
        return userDAO.register(user) ? null : "Registration failed. Please try again.";
    }

    public String login(String usernameOrEmail, String password) {
        if (!ValidationService.isNotEmpty(usernameOrEmail)) return "Username or email is required";
        if (!ValidationService.isNotEmpty(password)) return "Password is required";

        User user = userDAO.login(usernameOrEmail, password);
        if (user == null) return "Invalid credentials or account inactive";

        SessionManager.getInstance().login(user);
        return null;
    }

    public void logout() {
        SessionManager.getInstance().logout();
    }

    public boolean isLoggedIn() {
        return SessionManager.getInstance().isLoggedIn();
    }
}
