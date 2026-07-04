package com.ruyahotel.ui.components;

import com.ruyahotel.model.Room;
import com.ruyahotel.util.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * A card component displaying room information.
 */
public class RoomCard extends RoundedPanel {
    private Room room;
    private RoundedButton actionBtn;

    public RoomCard(Room room) {
        super(ModernTheme.CARD_RADIUS, ModernTheme.CARD_BG);
        this.room = room;
        setShowShadow(true);
        setLayout(new BorderLayout());

        // Image panel
        JPanel imgPanel = new JPanel(new BorderLayout());
        imgPanel.setPreferredSize(new Dimension(0, 160));
        imgPanel.setOpaque(false);
        displayRoomImage(imgPanel);
        add(imgPanel, BorderLayout.NORTH);

        // Info panel
        JPanel info = new JPanel(new GridBagLayout());
        info.setOpaque(false);
        info.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(4, 0, 4, 0);

        JLabel typeLbl = new JLabel(room.getRoomType());
        typeLbl.setFont(ModernTheme.FONT_SUBHEADING);
        typeLbl.setForeground(ModernTheme.TEXT_PRIMARY);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        info.add(typeLbl, gbc);

        JLabel priceLbl = new JLabel("ETB " + String.format("%,.2f", room.getPricePerNight()) + " / night");
        priceLbl.setFont(ModernTheme.FONT_TITLE);
        priceLbl.setForeground(ModernTheme.PRIMARY);
        gbc.gridy = 1;
        info.add(priceLbl, gbc);

        JLabel capLbl = new JLabel("\uD83D\uDC65 " + room.getCapacity() + " Guests");
        capLbl.setFont(ModernTheme.FONT_SMALL);
        capLbl.setForeground(ModernTheme.TEXT_SECONDARY);
        gbc.gridy = 2;
        info.add(capLbl, gbc);

        JLabel statusLbl = new JLabel(room.getStatus());
        statusLbl.setFont(ModernTheme.FONT_SMALL);
        if ("Available".equals(room.getStatus())) {
            statusLbl.setForeground(ModernTheme.SUCCESS);
        } else if ("Maintenance".equals(room.getStatus())) {
            statusLbl.setForeground(ModernTheme.DANGER);
        } else {
            statusLbl.setForeground(ModernTheme.WARNING);
        }
        gbc.gridy = 3;
        info.add(statusLbl, gbc);

        JLabel descLbl = new JLabel("<html><body style='width:200px'>" + room.getDescription() + "</body></html>");
        descLbl.setFont(ModernTheme.FONT_SMALL);
        descLbl.setForeground(ModernTheme.TEXT_SECONDARY);
        gbc.gridy = 4;
        info.add(descLbl, gbc);

        gbc.gridy = 5; gbc.fill = GridBagConstraints.HORIZONTAL;
        actionBtn = new RoundedButton("Book Now", ModernTheme.PRIMARY);
        if (!"Available".equals(room.getStatus())) {
            actionBtn.setEnabled(false);
            actionBtn = new RoundedButton("Unavailable", ModernTheme.TEXT_MUTED);
        }
        info.add(actionBtn, gbc);

        add(info, BorderLayout.CENTER);
    }

    private void displayRoomImage(JPanel container) {
        List<String> images = room.getImages();
        JLabel imgLabel = new JLabel();
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imgLabel.setOpaque(true);
        imgLabel.setBackground(ModernTheme.BORDER_LIGHT);

        if (images != null && !images.isEmpty()) {
            String imagePath = images.get(0);
            ImageIcon icon = ImageLoader.loadIcon(imagePath, 380, 160);
            if (icon != null) {
                imgLabel.setIcon(icon);
            } else {
                imgLabel.setText("\uD83C\uDFE8");
                imgLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
                imgLabel.setForeground(ModernTheme.TEXT_MUTED);
            }
        } else {
            imgLabel.setText("\uD83C\uDFE8");
            imgLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
            imgLabel.setForeground(ModernTheme.TEXT_MUTED);
        }

        container.add(imgLabel, BorderLayout.CENTER);
    }

    public Room getRoom() {
        return room;
    }

    public RoundedButton getActionButton() {
        return actionBtn;
    }
}
