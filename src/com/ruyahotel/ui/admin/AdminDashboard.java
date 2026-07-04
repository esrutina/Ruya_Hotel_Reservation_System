package com.ruyahotel.ui.admin;

import com.ruyahotel.ui.components.ModernTheme;
import com.ruyahotel.ui.components.SidebarItem;
import com.ruyahotel.util.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class AdminDashboard extends JPanel {
    private final Consumer<String> navigator;
    private final CardLayout cardLayout;
    private final JPanel contentPanel;

    public AdminDashboard(Consumer<String> navigator) {
        this.navigator = navigator;
        setLayout(new BorderLayout());
        setBackground(ModernTheme.BACKGROUND);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(ModernTheme.BACKGROUND);

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(ModernTheme.SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(24, 0, 24, 0));

        JLabel logo = new JLabel("\uD83C\uDFE8 Admin");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logo.setForeground(Color.WHITE);
        logo.setBorder(BorderFactory.createEmptyBorder(0, 20, 24, 20));
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(logo);

        SidebarItem[] items = {
                new SidebarItem("\uD83D\uDCCA", "Dashboard"),
                new SidebarItem("\uD83D\uDC64", "Users"),
                new SidebarItem("\uD83D\uDECF", "Rooms"),
                new SidebarItem("\uD83D\uDCCB", "Reservations"),
                new SidebarItem("\uD83D\uDCB3", "Payments"),
                new SidebarItem("\uD83D\uDCDD", "Feedback"),
                new SidebarItem("\uD83D\uDD13", "Roles"),
                new SidebarItem("\uD83D\uDCC8", "Reports"),
                new SidebarItem("\u2699", "Settings"),
                new SidebarItem("\uD83D\uDDD1", "Trash"),
                new SidebarItem("\uD83D\uDEAA", "Logout")
        };

        String[] targets = {
                "ADMIN_DASHBOARD", "ADMIN_USERS", "ADMIN_ROOMS", "ADMIN_RESERVATIONS",
                "ADMIN_PAYMENTS", "ADMIN_FEEDBACK", "ADMIN_ROLES", "ADMIN_REPORTS",
                "ADMIN_SETTINGS", "ADMIN_TRASH", "LOGOUT"
        };

        for (int i = 0; i < items.length; i++) {
            final int idx = i;
            items[i].onClick(() -> {
                if ("LOGOUT".equals(targets[idx])) {
                    SessionManager.getInstance().logout();
                    navigator.accept("HOME");
                } else {
                    cardLayout.show(contentPanel, targets[idx]);
                    for (SidebarItem s : items) s.setActive(false);
                    items[idx].setActive(true);
                }
            });
            items[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            sidebar.add(items[i]);
            sidebar.add(Box.createRigidArea(new Dimension(0, 2)));
        }

        contentPanel.add(new DashboardHome(), "ADMIN_DASHBOARD");
        contentPanel.add(new UserManagementPanel(navigator), "ADMIN_USERS");
        contentPanel.add(new RoomManagementPanel(navigator), "ADMIN_ROOMS");
        contentPanel.add(new ReservationManagementPanel(navigator), "ADMIN_RESERVATIONS");
        contentPanel.add(new PaymentManagementPanel(navigator), "ADMIN_PAYMENTS");
        contentPanel.add(new FeedbackManagementPanel(navigator), "ADMIN_FEEDBACK");
        contentPanel.add(new RoleManagementPanel(navigator), "ADMIN_ROLES");
        contentPanel.add(new ReportsPanel(navigator), "ADMIN_REPORTS");
        contentPanel.add(new SettingsPanel(navigator), "ADMIN_SETTINGS");
        contentPanel.add(new TrashPanel(navigator), "ADMIN_TRASH");

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }
}
