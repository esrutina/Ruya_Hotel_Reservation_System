package com.ruyahotel.ui.admin;

import com.ruyahotel.dao.RoleDAO;
import com.ruyahotel.model.Role;
import com.ruyahotel.model.User;
import com.ruyahotel.service.UserService;
import com.ruyahotel.ui.components.ModernTable;
import com.ruyahotel.ui.components.ModernTheme;
import com.ruyahotel.ui.components.RoundedButton;
import com.ruyahotel.ui.components.RoundedTextField;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class UserManagementPanel extends JPanel {
    private final Consumer<String> navigator;
    private final UserService userService = new UserService();
    private ModernTable table;
    private DefaultTableModel model;

    public UserManagementPanel(Consumer<String> navigator) {
        this.navigator = navigator;
        setLayout(new BorderLayout());
        setBackground(ModernTheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("User Management");
        title.setFont(ModernTheme.FONT_HEADING);
        title.setForeground(ModernTheme.TEXT_PRIMARY);
        add(title, BorderLayout.NORTH);

        String[] cols = {"ID", "Name", "Username", "Email", "Phone", "Role", "Status", "Actions"};
        model = new DefaultTableModel(cols, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        table = new ModernTable(model);
        add(table.wrapInScrollPane(), BorderLayout.CENTER);

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBar.setOpaque(false);

        RoundedTextField search = new RoundedTextField("Search users...");
        search.setPreferredSize(new Dimension(220, 36));
        topBar.add(search);

        RoundedButton addBtn = new RoundedButton("+ Add User", ModernTheme.SUCCESS);
        addBtn.setPreferredSize(new Dimension(120, 36));
        addBtn.addActionListener(e -> showAddUserDialog());
        topBar.add(addBtn);

        RoundedButton refreshBtn = new RoundedButton("Refresh", ModernTheme.INFO);
        refreshBtn.setPreferredSize(new Dimension(100, 36));
        refreshBtn.addActionListener(e -> loadUsers());
        topBar.add(refreshBtn);

        add(topBar, BorderLayout.NORTH);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.setOpaque(false);
        RoundedButton editBtn = new RoundedButton("Edit", ModernTheme.PRIMARY);
        editBtn.addActionListener(e -> editSelected());
        bottom.add(editBtn);

        RoundedButton toggleBtn = new RoundedButton("Toggle Status", ModernTheme.WARNING);
        toggleBtn.addActionListener(e -> toggleSelected());
        bottom.add(toggleBtn);

        RoundedButton resetBtn = new RoundedButton("Reset Password", ModernTheme.DANGER);
        resetBtn.addActionListener(e -> resetSelected());
        bottom.add(resetBtn);

        RoundedButton delBtn = new RoundedButton("Delete", ModernTheme.DANGER);
        delBtn.addActionListener(e -> deleteSelected());
        bottom.add(delBtn);

        add(bottom, BorderLayout.SOUTH);
        loadUsers();
    }

    private void loadUsers() {
        model.setRowCount(0);
        List<User> users = userService.getAllUsers();
        for (User u : users) {
            model.addRow(new Object[]{
                    u.getUserId(), u.getFullName(), u.getUsername(), u.getEmail(),
                    u.getPhone(), u.getRoleName(), u.getAccountStatus(), ""
            });
        }
    }

    private int getSelectedId() {
        int row = table.getSelectedRow();
        return row >= 0 ? (int) model.getValueAt(row, 0) : -1;
    }

    private void showAddUserDialog() {
        // Load roles dynamically from database
        RoleDAO roleDAO = new RoleDAO();
        List<Role> roles = roleDAO.getAll();
        JComboBox<String> role = new JComboBox<>();
        int[] roleIds = new int[roles.size()];
        for (int i = 0; i < roles.size(); i++) {
            role.addItem(roles.get(i).getRoleName());
            roleIds[i] = roles.get(i).getRoleId();
        }

        JTextField first = new JTextField(), last = new JTextField(), user = new JTextField();
        JTextField email = new JTextField(), phone = new JTextField();
        JPasswordField pass = new JPasswordField();
        Object[] msg = {
                "First Name:", first, "Last Name:", last, "Username:", user,
                "Email:", email, "Phone:", phone, "Password:", pass, "Role:", role
        };
        int opt = JOptionPane.showConfirmDialog(this, msg, "Add User", JOptionPane.OK_CANCEL_OPTION);
        if (opt == JOptionPane.OK_OPTION) {
            User newUser = new User();
            newUser.setFirstName(first.getText());
            newUser.setLastName(last.getText());
            newUser.setUsername(user.getText());
            newUser.setEmail(email.getText());
            newUser.setPhone(phone.getText());
            newUser.setPasswordHash(new String(pass.getPassword()));
            newUser.setRoleId(roleIds[role.getSelectedIndex()]);
            if (userService.addUser(newUser)) {
                JOptionPane.showMessageDialog(this, "User added!");
                loadUsers();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add user.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editSelected() {
        int id = getSelectedId();
        if (id < 0) return;
        User u = userService.getUser(id);
        if (u == null) return;

        // Load roles dynamically
        RoleDAO roleDAO = new RoleDAO();
        List<Role> roles = roleDAO.getAll();
        JComboBox<String> role = new JComboBox<>();
        int[] roleIds = new int[roles.size()];
        for (int i = 0; i < roles.size(); i++) {
            role.addItem(roles.get(i).getRoleName());
            roleIds[i] = roles.get(i).getRoleId();
        }
        // Set the current role as selected
        for (int i = 0; i < roleIds.length; i++) {
            if (roleIds[i] == u.getRoleId()) {
                role.setSelectedIndex(i);
                break;
            }
        }

        JTextField phone = new JTextField(u.getPhone());
        Object[] msg = {"Phone:", phone, "Role:", role};
        int opt = JOptionPane.showConfirmDialog(this, msg, "Edit User", JOptionPane.OK_CANCEL_OPTION);
        if (opt == JOptionPane.OK_OPTION) {
            u.setPhone(phone.getText());
            userService.updateProfile(u);
            userService.changeRole(id, roleIds[role.getSelectedIndex()]);
            loadUsers();
        }
    }

    private void toggleSelected() {
        int id = getSelectedId();
        if (id < 0) return;
        if (userService.toggleStatus(id)) {
            JOptionPane.showMessageDialog(this, "Status updated.");
            loadUsers();
        }
    }

    private void resetSelected() {
        int id = getSelectedId();
        if (id < 0) return;
        JPasswordField p1 = new JPasswordField(), p2 = new JPasswordField();
        Object[] msg = {"New Password:", p1, "Confirm:", p2};
        int opt = JOptionPane.showConfirmDialog(this, msg, "Reset Password", JOptionPane.OK_CANCEL_OPTION);
        if (opt == JOptionPane.OK_OPTION) {
            String n = new String(p1.getPassword());
            String c = new String(p2.getPassword());
            if (!n.equals(c)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.");
                return;
            }
            userService.adminResetPassword(id, n);
            JOptionPane.showMessageDialog(this, "Password reset.");
        }
    }

    private void deleteSelected() {
        int id = getSelectedId();
        if (id < 0) return;
        int confirm = JOptionPane.showConfirmDialog(this, "Move user to trash?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // Use current admin id from session
            com.ruyahotel.model.User admin = com.ruyahotel.util.SessionManager.getInstance().getCurrentUser();
            int adminId = admin != null ? admin.getUserId() : 1;
            userService.deleteUser(id, adminId);
            JOptionPane.showMessageDialog(this, "User moved to trash.");
            loadUsers();
        }
    }
}
