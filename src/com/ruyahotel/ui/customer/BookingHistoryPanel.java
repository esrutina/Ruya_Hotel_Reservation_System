package com.ruyahotel.ui.customer;

import com.ruyahotel.model.Reservation;
import com.ruyahotel.service.ReservationService;
import com.ruyahotel.ui.components.ModernTable;
import com.ruyahotel.ui.components.ModernTheme;
import com.ruyahotel.util.SessionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class BookingHistoryPanel extends JPanel {
    private final Consumer<String> navigator;
    private final ReservationService reservationService = new ReservationService();

    public BookingHistoryPanel(Consumer<String> navigator) {
        this.navigator = navigator;
        setLayout(new BorderLayout());
        setBackground(ModernTheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("Booking History");
        title.setFont(ModernTheme.FONT_HEADING);
        title.setForeground(ModernTheme.TEXT_PRIMARY);
        add(title, BorderLayout.NORTH);

        String[] cols = {"ID", "Room", "Check-in", "Check-out", "Total (ETB)", "Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        ModernTable table = new ModernTable(model);

        List<Reservation> list = reservationService.getUserReservations(SessionManager.getInstance().getUserId());
        for (Reservation r : list) {
            model.addRow(new Object[]{
                    r.getReservationId(), r.getRoomType(), r.getCheckInDate(),
                    r.getCheckOutDate(), String.format("%,.2f", r.getTotalPrice()), r.getStatus()
            });
        }

        add(table.wrapInScrollPane(), BorderLayout.CENTER);
    }
}
