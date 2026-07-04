package com.ruyahotel.ui.customer;

import com.ruyahotel.model.Reservation;
import com.ruyahotel.service.ReservationService;
import com.ruyahotel.ui.MainFrame;
import com.ruyahotel.ui.components.ModernTable;
import com.ruyahotel.ui.components.ModernTheme;
import com.ruyahotel.ui.components.RoundedButton;
import com.ruyahotel.util.SessionManager;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ReservationPanel extends JPanel {
    private final Consumer<String> navigator;
    private final ReservationService reservationService = new ReservationService();
    private ModernTable table;
    private DefaultTableModel model;

    public ReservationPanel(Consumer<String> navigator) {
        this.navigator = navigator;
        setLayout(new BorderLayout());
        setBackground(ModernTheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("My Reservations");
        title.setFont(ModernTheme.FONT_HEADING);
        title.setForeground(ModernTheme.TEXT_PRIMARY);
        add(title, BorderLayout.NORTH);

        String[] cols = {"ID", "Room", "Check-in", "Check-out", "Days", "Total (ETB)", "Status", "Payment"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new ModernTable(model);
        add(table.wrapInScrollPane(), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.setOpaque(false);
        RoundedButton refreshBtn = new RoundedButton("Refresh", ModernTheme.INFO);
        refreshBtn.setPreferredSize(new Dimension(120, 36));
        refreshBtn.addActionListener(e -> loadReservations());
        bottom.add(refreshBtn);

        RoundedButton cancelBtn = new RoundedButton("Cancel Selected", ModernTheme.DANGER);
        cancelBtn.setPreferredSize(new Dimension(160, 36));
        cancelBtn.addActionListener(e -> cancelSelected());
        bottom.add(cancelBtn);

        RoundedButton payBtn = new RoundedButton("Pay Selected", ModernTheme.SUCCESS);
        payBtn.setPreferredSize(new Dimension(140, 36));
        payBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int resId = (int) model.getValueAt(row, 0);
                Window window = SwingUtilities.getWindowAncestor(this);
                if (window instanceof MainFrame) {
                    ((MainFrame) window).navigateToPaymentFromBooking(resId);
                } else {
                    navigator.accept("CUSTOMER_PAYMENT");
                }
            }
        });
        bottom.add(payBtn);

        add(bottom, BorderLayout.SOUTH);
        loadReservations();
    }

    private void loadReservations() {
        model.setRowCount(0);
        List<Reservation> list = reservationService.getUserReservations(SessionManager.getInstance().getUserId());
        for (Reservation r : list) {
            model.addRow(new Object[]{
                    r.getReservationId(),
                    r.getRoomType(),
                    r.getCheckInDate(),
                    r.getCheckOutDate(),
                    r.getTotalDays(),
                    String.format("%,.2f", r.getTotalPrice()),
                    r.getStatus(),
                    r.getPaymentStatus()
            });
        }
    }

    private void cancelSelected() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        int resId = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Cancel this reservation?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        String error = reservationService.cancelReservation(resId, SessionManager.getInstance().getUserId());
        if (error != null) {
            JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Reservation cancelled.");
            loadReservations();
        }
    }
}
