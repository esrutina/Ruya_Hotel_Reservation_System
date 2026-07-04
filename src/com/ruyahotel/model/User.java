package com.ruyahotel.model;

import java.time.LocalDateTime;

public class User {
    private int userId;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phone;
    private String passwordHash;
    private String gender;
    private String nationality;
    private String address;
    private String profilePicture;
    private int roleId;
    private String accountStatus;
    private LocalDateTime registrationDate;
    private LocalDateTime lastLogin;

    // For display
    private String roleName;

    public User() {}

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFullName() { return firstName + " " + lastName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }

    public int getRoleId() { return roleId; }
    public void setRoleId(int roleId) { this.roleId = roleId; }

    public String getAccountStatus() { return accountStatus; }
    public void setAccountStatus(String accountStatus) { this.accountStatus = accountStatus; }

    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }

    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }

    public boolean isActive() {
        return "Active".equals(accountStatus);
    }

    public boolean isAdmin() {
        return roleId == 1;
    }
}
