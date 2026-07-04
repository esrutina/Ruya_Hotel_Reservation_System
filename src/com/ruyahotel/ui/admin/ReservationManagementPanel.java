package com.ruyahotel.ui.admin;

import com.ruyahotel.model.Reservation;
import com.ruyahotel.service.ReservationService;
import com.ruyahotel.ui.components.ModernTable;
import com.ruyahotel.ui.components.ModernTheme;
import com.ruyahotel.ui.components.RoundedButton;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class ReservationManagementPanel extends JPanel {
    private final Consumer<String> navigator;
    private final ReservationService reservationService = new ReservationService();
    private ModernTable table;
    private DefaultTableModel model;

    public ReservationManagementPanel(Consumer<String> navigator) {
        this.navigator = navigator;
        setLayout(new BorderLayout());
        setBackground(ModernTheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("Reservation Management");
        title.setFont(ModernTheme.FONT_HEADING);
        title.setForeground(ModernTheme.TEXT_PRIMARY);
        add(title, BorderLayout.NORTH);

        String[] cols = {"ID", "Customer", "Room", "Check-in", "Check-out", "Days", "Total", "Status", "Payment"};
        model = new DefaultTableModel(cols, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        table = new ModernTable(model);
        add(table.wrapInScrollPane(), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.setOpaque(false);

        RoundedButton confirmBtn = new RoundedButton("Confirm", ModernTheme.SUCCESS);
        confirmBtn.addActionListener(e -> { int id = getSelectedId(); if (id > 0) { reservationService.confirmReservation(id); loadAll(); } });
        bottom.add(confirmBtn);

        RoundedButton checkinBtn = new RoundedButton("Check-In", ModernTheme.PRIMARY);
        checkinBtn.addActionListener(e -> { int id = getSelectedId(); if (id > 0) { reservationService.checkIn(id); loadAll(); } });
        bottom.add(checkinBtn);

        RoundedButton checkoutBtn = new RoundedButton("Check-Out", ModernTheme.INFO);
        checkoutBtn.addActionListener(e -> { int id = getSelectedId(); if (id > 0) { reservationService.checkOut(id); loadAll(); } });
        bottom.add(checkoutBtn);

        RoundedButton cancelBtn = new RoundedButton("Cancel", ModernTheme.DANGER);
        cancelBtn.addActionListener(e -> { int id = getSelectedId(); if (id > 0) { reservationService.adminCancel(id); loadAll(); } });
        bottom.add(cancelBtn);

        RoundedButton refreshBtn = new RoundedButton("Refresh", ModernTheme.WARNING);
        refreshBtn.addActionListener(e -> loadAll());
        bottom.add(refreshBtn);

        add(bottom, BorderLayout.SOUTH);
        loadAll();
    }

    private void loadAll() {
        model.setRowCount(0);
        List<Reservation> list = reservationService.getAllReservations();
        for (Reservation r : list) {
            model.addRow(new Object[]{
                    r.getReservationId(), r.getCustomerName(), r.getRoomType(),
                    r.getCheckInDate(), r.getCheckOutDate(), r.getTotalDays(),
                    String.format("%,.2f", r.getTotalPrice()), r.getStatus(), r.getPaymentStatus()
            });
        }
    }

    private int getSelectedId() {
        int row = table.getSelectedRow();
        return row >= 0 ? (int) model.getValueAt(row, 0) : -1;
    }
}
