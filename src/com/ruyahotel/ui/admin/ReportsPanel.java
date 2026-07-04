package com.ruyahotel.ui.admin;

import com.ruyahotel.service.ReportService;
import com.ruyahotel.ui.components.*;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.function.Consumer;

public class ReportsPanel extends JPanel {
    private final Consumer<String> navigator;
    private final ReportService reportService = new ReportService();

    public ReportsPanel(Consumer<String> navigator) {
        this.navigator = navigator;
        setLayout(new BorderLayout());
        setBackground(ModernTheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("Reports");
        title.setFont(ModernTheme.FONT_HEADING);
        title.setForeground(ModernTheme.TEXT_PRIMARY);
        add(title, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(ModernTheme.FONT_TITLE);
        tabs.setBackground(ModernTheme.CARD_BG);
        tabs.addTab("Overview", overviewPanel());
        tabs.addTab("Bookings", bookingsPanel());
        tabs.addTab("Rooms", roomsPanel());
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel overviewPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ModernTheme.BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.BOTH;

        Map<String, Object> stats = reportService.getDashboardStats();

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new StatCard("\uD83D\uDCCB", String.valueOf(stats.get("totalReservations")), "Total Bookings", ModernTheme.PRIMARY), gbc);
        gbc.gridx = 1;
        panel.add(new StatCard("\u2705", String.valueOf(stats.get("confirmedReservations")), "Confirmed", ModernTheme.SUCCESS), gbc);
        gbc.gridx = 2;
        panel.add(new StatCard("\u274C", String.valueOf(stats.get("cancelledReservations")), "Cancelled", ModernTheme.DANGER), gbc);
        gbc.gridx = 3;
        panel.add(new StatCard("\u23F0", String.valueOf(stats.get("expiredReservations")), "Expired", ModernTheme.WARNING), gbc);

        gbc.gridy = 1; gbc.gridx = 0; gbc.gridwidth = 2;
        ChartPanel barChart = new ChartPanel();
        barChart.setPreferredSize(new Dimension(400, 250));
        Map<String, Integer> bookingMap = reportService.getBookingStatusDistribution();
        barChart.setBarData(
                bookingMap.keySet().toArray(new String[0]),
                bookingMap.values().stream().mapToDouble(Integer::doubleValue).toArray(),
                new Color[]{ModernTheme.PRIMARY, ModernTheme.WARNING, ModernTheme.SUCCESS, ModernTheme.DANGER, ModernTheme.TEXT_MUTED},
                "Booking Status"
        );
        panel.add(barChart, gbc);

        gbc.gridx = 2; gbc.gridwidth = 2;
        ChartPanel pieChart = new ChartPanel();
        pieChart.setPreferredSize(new Dimension(400, 250));
        Map<String, Integer> roomMap = reportService.getRoomStatusDistribution();
        pieChart.setPieData(
                roomMap.keySet().toArray(new String[0]),
                roomMap.values().stream().mapToDouble(Integer::doubleValue).toArray(),
                new Color[]{ModernTheme.SUCCESS, ModernTheme.WARNING, ModernTheme.DANGER, ModernTheme.TEXT_MUTED},
                "Room Status"
        );
        panel.add(pieChart, gbc);

        return panel;
    }

    private JPanel bookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ModernTheme.BACKGROUND);
        Map<String, Object> stats = reportService.getDashboardStats();
        JTextArea area = new JTextArea();
        area.setFont(new Font("Consolas", Font.PLAIN, 14));
        area.setEditable(false);
        area.setBackground(ModernTheme.CARD_BG);
        area.setForeground(ModernTheme.TEXT_PRIMARY);
        area.setText(
                "BOOKING REPORT\n" +
                        "================\n\n" +
                        "Total Bookings:      " + stats.get("totalReservations") + "\n" +
                        "Confirmed:           " + stats.get("confirmedReservations") + "\n" +
                        "Pending:             " + stats.get("pendingReservations") + "\n" +
                        "Checked-In:          " + stats.get("checkedInReservations") + "\n" +
                        "Cancelled:           " + stats.get("cancelledReservations") + "\n" +
                        "Expired:             " + stats.get("expiredReservations") + "\n\n" +
                        "Today's Check-ins:   " + stats.get("todayCheckIns") + "\n" +
                        "Today's Check-outs:  " + stats.get("todayCheckOuts") + "\n"
        );
        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        return panel;
    }

    private JPanel roomsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ModernTheme.BACKGROUND);
        Map<String, Object> stats = reportService.getDashboardStats();
        JTextArea area = new JTextArea();
        area.setFont(new Font("Consolas", Font.PLAIN, 14));
        area.setEditable(false);
        area.setBackground(ModernTheme.CARD_BG);
        area.setForeground(ModernTheme.TEXT_PRIMARY);
        area.setText(
                "ROOM REPORT\n" +
                        "===========\n\n" +
                        "Total Rooms:         " + stats.get("totalRooms") + "\n" +
                        "Available:           " + stats.get("availableRooms") + "\n" +
                        "Occupied:            " + stats.get("occupiedRooms") + "\n" +
                        "Maintenance:         " + stats.get("maintenanceRooms") + "\n\n" +
                        "Total Revenue:       ETB " + String.format("%,.2f", (Double) stats.get("totalRevenue")) + "\n"
        );
        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        return panel;
    }
}
