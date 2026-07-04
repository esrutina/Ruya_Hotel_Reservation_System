package com.ruyahotel.ui.admin;

import com.ruyahotel.model.Role;
import com.ruyahotel.service.UserService;
import com.ruyahotel.ui.components.ModernTable;
import com.ruyahotel.ui.components.ModernTheme;
import com.ruyahotel.ui.components.RoundedButton;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class RoleManagementPanel extends JPanel {
    private final Consumer<String> navigator;
    private final com.ruyahotel.dao.RoleDAO roleDAO = new com.ruyahotel.dao.RoleDAO();
    private ModernTable table;
    private DefaultTableModel model;

    public RoleManagementPanel(Consumer<String> navigator) {
        this.navigator = navigator;
        setLayout(new BorderLayout());
        setBackground(ModernTheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("Role Management");
        title.setFont(ModernTheme.FONT_HEADING);
        title.setForeground(ModernTheme.TEXT_PRIMARY);
        add(title, BorderLayout.NORTH);

        String[] cols = {"ID", "Role Name", "Description", "Permissions", "Status"};
        model = new DefaultTableModel(cols, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        table = new ModernTable(model);
        add(table.wrapInScrollPane(), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.setOpaque(false);
        RoundedButton addBtn = new RoundedButton("+ Add Role", ModernTheme.SUCCESS);
        addBtn.addActionListener(e -> showAddRoleDialog());
        bottom.add(addBtn);
        RoundedButton refreshBtn = new RoundedButton("Refresh", ModernTheme.INFO);
        refreshBtn.addActionListener(e -> loadRoles());
        bottom.add(refreshBtn);
        add(bottom, BorderLayout.SOUTH);
        loadRoles();
    }

    private void loadRoles() {
        model.setRowCount(0);
        List<Role> list = roleDAO.getAll();
        for (Role r : list) {
            model.addRow(new Object[]{r.getRoleId(), r.getRoleName(), r.getDescription(),
                    String.join(", ", r.getPermissions()), r.getStatus()});
        }
    }

    private void showAddRoleDialog() {
        JTextField name = new JTextField();
        JTextArea desc = new JTextArea(2, 20);
        JCheckBox[] perms = {
                new JCheckBox("Users"), new JCheckBox("Rooms"), new JCheckBox("Reservations"),
                new JCheckBox("Payments"), new JCheckBox("Feedback"), new JCheckBox("Reports")
        };
        JPanel permPanel = new JPanel(new GridLayout(0, 2));
        for (JCheckBox p : perms) permPanel.add(p);
        Object[] msg = {"Role Name:", name, "Description:", new JScrollPane(desc), "Permissions:", permPanel};
        int opt = JOptionPane.showConfirmDialog(this, msg, "Add Role", JOptionPane.OK_CANCEL_OPTION);
        if (opt == JOptionPane.OK_OPTION) {
            Role r = new Role();
            r.setRoleName(name.getText().trim());
            r.setDescription(desc.getText());
            java.util.List<String> list = new java.util.ArrayList<>();
            for (JCheckBox p : perms) if (p.isSelected()) list.add(p.getText().toLowerCase());
            r.setPermissions(list);
            r.setStatus("Active");
            if (roleDAO.create(r)) {
                JOptionPane.showMessageDialog(this, "Role added!");
                loadRoles();
            } else {
                JOptionPane.showMessageDialog(this, "Failed. Name may exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
