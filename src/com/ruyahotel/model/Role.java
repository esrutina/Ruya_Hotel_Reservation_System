package com.ruyahotel.model;

import java.time.LocalDateTime;
import java.util.List;

public class Role {
    private int roleId;
    private String roleName;
    private String description;
    private List<String> permissions;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Role() {}

    public int getRoleId() { return roleId; }
    public void setRoleId(int roleId) { this.roleId = roleId; }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getPermissions() { return permissions; }
    public void setPermissions(List<String> permissions) { this.permissions = permissions; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public boolean isAdminRole() {
        return "Admin".equalsIgnoreCase(roleName);
    }
}
