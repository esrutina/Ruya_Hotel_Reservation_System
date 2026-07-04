package com.ruyahotel.ui.admin;

import com.ruyahotel.service.ReportService;
import com.ruyahotel.ui.components.*;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class DashboardHome extends JPanel {
    private final ReportService reportService = new ReportService();

    public DashboardHome() {
        setLayout(new BorderLayout());
        setBackground(ModernTheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("Admin Dashboard");
        title.setFont(ModernTheme.FONT_HEADING);
        title.setForeground(ModernTheme.TEXT_PRIMARY);
        add(title, BorderLayout.NORTH);

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(ModernTheme.BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.BOTH;

        Map<String, Object> stats = reportService.getDashboardStats();

        gbc.gridx = 0; gbc.gridy = 0;
        content.add(new StatCard("\uD83D\uDC64", String.valueOf(stats.get("totalUsers")), "Total Users", ModernTheme.PRIMARY), gbc);
        gbc.gridx = 1;
        content.add(new StatCard("\uD83D\uDECF", String.valueOf(stats.get("totalRooms")), "Total Rooms", ModernTheme.INFO), gbc);
        gbc.gridx = 2;
        content.add(new StatCard("\uD83D\uDCCB", String.valueOf(stats.get("totalReservations")), "Reservations", ModernTheme.WARNING), gbc);
        gbc.gridx = 3;
        content.add(new StatCard("\uD83D\uDCB0", "ETB " + String.format("%,.0f", (Double) stats.get("totalRevenue")), "Total Revenue", ModernTheme.SUCCESS), gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        content.add(new StatCard("\uD83D\uDCC5", String.valueOf(stats.get("todayCheckIns")), "Today's Check-ins", ModernTheme.ACCENT), gbc);
        gbc.gridx = 1;
        content.add(new StatCard("\uD83D\uDD70", String.valueOf(stats.get("todayCheckOuts")), "Today's Check-outs", ModernTheme.DANGER), gbc);
        gbc.gridx = 2;
        content.add(new StatCard("\u2B50", String.format("%.1f", (Double) stats.get("averageRating")), "Avg Rating", ModernTheme.PRIMARY_LIGHT), gbc);
        gbc.gridx = 3;
        content.add(new StatCard("\uD83D\uDD12", String.valueOf(stats.get("occupiedRooms")), "Occupied Rooms", ModernTheme.TEXT_SECONDARY), gbc);

        // Recent activity placeholder
        gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 4;
        JLabel recent = new JLabel("Use the sidebar to manage Users, Rooms, Reservations, Payments, and more.");
        recent.setFont(ModernTheme.FONT_BODY);
        recent.setForeground(ModernTheme.TEXT_SECONDARY);
        content.add(recent, gbc);

        add(content, BorderLayout.CENTER);
    }
}
