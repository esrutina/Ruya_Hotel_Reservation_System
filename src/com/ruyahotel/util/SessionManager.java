package com.ruyahotel.util;

import com.ruyahotel.model.User;

/**
 * Singleton session manager to track logged-in user across the application.
 */
public class SessionManager {
    private static SessionManager instance;
    private User currentUser;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void login(User user) {
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean isAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }

    public int getUserId() {
        return currentUser != null ? currentUser.getUserId() : -1;
    }
    public String getEmail() {
        return currentUser != null ? currentUser.getEmail() : "";
    }

    public String getFullName() {
        if (currentUser == null) return "";

        return currentUser.getFirstName() + " "
                + currentUser.getLastName();
    }
}
