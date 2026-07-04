package com.ruyahotel.ui.customer;

import com.ruyahotel.model.Reservation;
import com.ruyahotel.model.Room;
import com.ruyahotel.service.ReservationService;
import com.ruyahotel.service.RoomService;
import com.ruyahotel.ui.MainFrame;
import com.ruyahotel.ui.components.*;
import com.ruyahotel.util.SessionManager;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.function.Consumer;

public class BookRoomPanel extends JPanel {
    private final Consumer<String> navigator;
    private final RoomService roomService = new RoomService();
    private final ReservationService reservationService = new ReservationService();
    private JPanel roomsGrid;
    private JComboBox<String> roomTypeFilter;
    private RoundedTextField nameField, emailField, phoneField;
    private JDateChooser checkInChooser, checkOutChooser;
    private JLabel priceLabel;
    private Room selectedRoom;

    public BookRoomPanel(Consumer<String> navigator) {
        this.navigator = navigator;
        setLayout(new BorderLayout());
        setBackground(ModernTheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("Book a Room");
        title.setFont(ModernTheme.FONT_HEADING);
        title.setForeground(ModernTheme.TEXT_PRIMARY);
        add(title, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, roomsListPanel(), bookingFormPanel());
        split.setDividerLocation(620);
        split.setBorder(null);
        split.setBackground(ModernTheme.BACKGROUND);
        add(split, BorderLayout.CENTER);

        refreshRooms();
    }

    private JPanel roomsListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ModernTheme.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 16));

        JPanel filter = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filter.setOpaque(false);
        filter.add(new JLabel("Filter:"));
        roomTypeFilter = new JComboBox<>(new String[]{"All", "Standard", "Double", "Deluxe", "Family", "Presidential Suite"});
        roomTypeFilter.setFont(ModernTheme.FONT_BODY);
        roomTypeFilter.addActionListener(e -> refreshRooms());
        filter.add(roomTypeFilter);
        panel.add(filter, BorderLayout.NORTH);

        roomsGrid = new JPanel(new GridLayout(0, 2, 16, 16));
        roomsGrid.setOpaque(false);
        JScrollPane scroll = new JScrollPane(roomsGrid);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(ModernTheme.BACKGROUND);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel bookingFormPanel() {
        RoundedPanel form = new RoundedPanel(ModernTheme.CARD_RADIUS, ModernTheme.CARD_BG);
        form.setShowShadow(true);
        form.setLayout(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel formTitle = new JLabel("Reservation Details");
        formTitle.setFont(ModernTheme.FONT_SUBHEADING);
        formTitle.setForeground(ModernTheme.TEXT_PRIMARY);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        form.add(formTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0; form.add(fieldLabel("Full Name"), gbc);
        gbc.gridx = 1; nameField = new RoundedTextField(20); form.add(nameField, gbc);

        gbc.gridy = 2; gbc.gridx = 0; form.add(fieldLabel("Email"), gbc);
        gbc.gridx = 1; emailField = new RoundedTextField(20); form.add(emailField, gbc);

        gbc.gridy = 3; gbc.gridx = 0; form.add(fieldLabel("Phone"), gbc);
        gbc.gridx = 1; phoneField = new RoundedTextField(20); form.add(phoneField, gbc);

        gbc.gridy = 4; gbc.gridx = 0; form.add(fieldLabel("Check-in"), gbc);
        gbc.gridx = 1; checkInChooser = new JDateChooser(); checkInChooser.setDateFormatString("yyyy-MM-dd"); form.add(checkInChooser, gbc);

        gbc.gridy = 5; gbc.gridx = 0; form.add(fieldLabel("Check-out"), gbc);
        gbc.gridx = 1; checkOutChooser = new JDateChooser(); checkOutChooser.setDateFormatString("yyyy-MM-dd"); form.add(checkOutChooser, gbc);

        gbc.gridy = 6; gbc.gridx = 0; form.add(fieldLabel("Selected Room"), gbc);
        gbc.gridx = 1; priceLabel = new JLabel("None selected");
        priceLabel.setFont(ModernTheme.FONT_TITLE);
        priceLabel.setForeground(ModernTheme.PRIMARY);
        form.add(priceLabel, gbc);

        gbc.gridy = 7; gbc.gridx = 0; gbc.gridwidth = 2;
        RoundedButton bookBtn = new RoundedButton("Confirm Booking", ModernTheme.PRIMARY);
        bookBtn.setPreferredSize(new Dimension(0, 44));
        bookBtn.addActionListener(e -> bookRoom());
        form.add(bookBtn, gbc);

        return form;
    }

    private void refreshRooms() {
        roomsGrid.removeAll();
        String filter = (String) roomTypeFilter.getSelectedItem();
        java.util.List<Room> rooms;
        if ("All".equals(filter)) {
            rooms = roomService.getAvailableRooms();
        } else {
            rooms = roomService.getAllRooms();
            rooms.removeIf(r -> !r.getRoomType().equals(filter));
        }
        for (Room room : rooms) {
            RoomCard card = new RoomCard(room);
            card.getActionButton().setText("Select");
            if (!"Available".equals(room.getStatus())) card.getActionButton().setEnabled(false);
            card.getActionButton().addActionListener(e -> {
                selectedRoom = room;
                priceLabel.setText(room.getRoomType() + " - ETB " + String.format("%,.2f", room.getPricePerNight()) + "/night");
            });
            roomsGrid.add(card);
        }
        roomsGrid.revalidate();
        roomsGrid.repaint();
    }

    private void bookRoom() {
        if (selectedRoom == null) {
            JOptionPane.showMessageDialog(this, "Please select a room first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (checkInChooser.getDate() == null || checkOutChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please select check-in and check-out dates.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        LocalDate checkIn = checkInChooser.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        LocalDate checkOut = checkOutChooser.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
            JOptionPane.showMessageDialog(this, "Check-out must be after check-in.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Reservation r = new Reservation();
        r.setUserId(SessionManager.getInstance().getUserId());
        r.setRoomId(selectedRoom.getRoomId());
        r.setFullName(nameField.getText().trim());
        r.setEmail(emailField.getText().trim());
        r.setPhone(phoneField.getText().trim());
        r.setCheckInDate(checkIn);
        r.setCheckOutDate(checkOut);

        int reservationId = reservationService.createReservation(r);
        if (reservationId <= 0) {
            JOptionPane.showMessageDialog(this, "Failed to create reservation.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Reservation created! Proceed to payment.", "Success", JOptionPane.INFORMATION_MESSAGE);

        // Navigate to dashboard first, then show payment with the reservation selected
        navigator.accept("CUSTOMER_DASHBOARD");
        // Now show payment panel within dashboard
        SwingUtilities.invokeLater(() -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof MainFrame) {
                // Find the dashboard panel and trigger its internal navigation
                ((MainFrame) window).navigateToPaymentFromBooking(reservationId);
            }
        });
    }

    private JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(ModernTheme.FONT_TITLE);
        l.setForeground(ModernTheme.TEXT_PRIMARY);
        return l;
    }
}
