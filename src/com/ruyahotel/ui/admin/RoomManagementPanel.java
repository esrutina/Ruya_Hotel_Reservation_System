package com.ruyahotel.ui.admin;

import com.ruyahotel.model.Room;
import com.ruyahotel.service.RoomService;
import com.ruyahotel.ui.components.ModernTable;
import com.ruyahotel.ui.components.ModernTheme;
import com.ruyahotel.ui.components.RoundedButton;
import com.ruyahotel.ui.components.RoundedTextField;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class RoomManagementPanel extends JPanel {
    private final Consumer<String> navigator;
    private final RoomService roomService = new RoomService();
    private ModernTable table;
    private DefaultTableModel model;

    public RoomManagementPanel(Consumer<String> navigator) {
        this.navigator = navigator;
        setLayout(new BorderLayout());
        setBackground(ModernTheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("Room Management");
        title.setFont(ModernTheme.FONT_HEADING);
        title.setForeground(ModernTheme.TEXT_PRIMARY);
        add(title, BorderLayout.NORTH);

        String[] cols = {"ID", "Room #", "Type", "Price", "Capacity", "Status", "Actions"};
        model = new DefaultTableModel(cols, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        table = new ModernTable(model);
        add(table.wrapInScrollPane(), BorderLayout.CENTER);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setOpaque(false);
        RoundedButton addBtn = new RoundedButton("+ Add Room", ModernTheme.SUCCESS);
        addBtn.setPreferredSize(new Dimension(120, 36));
        addBtn.addActionListener(e -> showAddRoomDialog());
        top.add(addBtn);

        RoundedButton refreshBtn = new RoundedButton("Refresh", ModernTheme.INFO);
        refreshBtn.setPreferredSize(new Dimension(100, 36));
        refreshBtn.addActionListener(e -> loadRooms());
        top.add(refreshBtn);
        add(top, BorderLayout.NORTH);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.setOpaque(false);
        RoundedButton editBtn = new RoundedButton("Edit", ModernTheme.PRIMARY);
        editBtn.addActionListener(e -> editSelected());
        bottom.add(editBtn);

        RoundedButton maintBtn = new RoundedButton("Set Maintenance", ModernTheme.WARNING);
        maintBtn.addActionListener(e -> {
            int id = getSelectedId(); if (id > 0) { roomService.setMaintenance(id); loadRooms(); }
        });
        bottom.add(maintBtn);

        RoundedButton availBtn = new RoundedButton("Set Available", ModernTheme.SUCCESS);
        availBtn.addActionListener(e -> {
            int id = getSelectedId(); if (id > 0) { roomService.setAvailable(id); loadRooms(); }
        });
        bottom.add(availBtn);

        RoundedButton delBtn = new RoundedButton("Delete", ModernTheme.DANGER);
        delBtn.addActionListener(e -> deleteSelected());
        bottom.add(delBtn);
        add(bottom, BorderLayout.SOUTH);

        loadRooms();
    }

    private void loadRooms() {
        model.setRowCount(0);
        for (Room r : roomService.getAllRooms()) {
            model.addRow(new Object[]{r.getRoomId(), r.getRoomNumber(), r.getRoomType(),
                    String.format("%,.2f", r.getPricePerNight()), r.getCapacity(), r.getStatus(), ""});
        }
    }

    private int getSelectedId() {
        int row = table.getSelectedRow();
        return row >= 0 ? (int) model.getValueAt(row, 0) : -1;
    }

    private void showAddRoomDialog() {
        RoundedTextField num = new RoundedTextField("101");
        JComboBox<String> type = new JComboBox<>(new String[]{"Standard","Double","Deluxe","Family","Presidential Suite"});
        RoundedTextField price = new RoundedTextField("2500");
        RoundedTextField cap = new RoundedTextField("2");
        JTextArea desc = new JTextArea(3, 20);
        JComboBox<String> status = new JComboBox<>(new String[]{"Available","Maintenance"});
        Object[] msg = {
                "Room Number:", num, "Type:", type, "Price/Night:", price,
                "Capacity:", cap, "Description:", new JScrollPane(desc), "Status:", status
        };
        int opt = JOptionPane.showConfirmDialog(this, msg, "Add Room", JOptionPane.OK_CANCEL_OPTION);
        if (opt == JOptionPane.OK_OPTION) {
            Room r = new Room();
            r.setRoomNumber(num.getText().trim());
            r.setRoomType((String) type.getSelectedItem());
            r.setPricePerNight(Double.parseDouble(price.getText().trim()));
            r.setCapacity(Integer.parseInt(cap.getText().trim()));
            r.setDescription(desc.getText());
            r.setFeatures(Arrays.asList("WiFi", "TV", "AC"));
            String imageFile = ((String) type.getSelectedItem()).toLowerCase().replace(" ", "_");
            if ("presidential suite".equals(imageFile)) {
                imageFile = "presidential";
            }
            imageFile += ".jpg";
            r.setImages(Arrays.asList(imageFile));
            r.setStatus((String) status.getSelectedItem());
            if (roomService.addRoom(r)) {
                JOptionPane.showMessageDialog(this, "Room added!");
                loadRooms();
            } else {
                JOptionPane.showMessageDialog(this, "Failed. Room number may exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editSelected() {
        int id = getSelectedId();
        if (id < 0) return;
        Room r = roomService.getRoom(id);
        if (r == null) return;
        RoundedTextField price = new RoundedTextField(String.valueOf(r.getPricePerNight()));
        JComboBox<String> status = new JComboBox<>(new String[]{"Available","Reserved","Occupied","Maintenance"});
        status.setSelectedItem(r.getStatus());
        Object[] msg = {"Price:", price, "Status:", status};
        int opt = JOptionPane.showConfirmDialog(this, msg, "Edit Room", JOptionPane.OK_CANCEL_OPTION);
        if (opt == JOptionPane.OK_OPTION) {
            r.setPricePerNight(Double.parseDouble(price.getText().trim()));
            r.setStatus((String) status.getSelectedItem());
            roomService.updateRoom(r);
            loadRooms();
        }
    }

    private void deleteSelected() {
        int id = getSelectedId();
        if (id < 0) return;
        int confirm = JOptionPane.showConfirmDialog(this, "Delete room?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            roomService.deleteRoom(id);
            JOptionPane.showMessageDialog(this, "Room deleted.");
            loadRooms();
        }
    }
}
