package com.ruyahotel.ui.components;

import javax.swing.*;
import java.awt.*;

/**
 * A dashboard stat card showing a metric with icon, value, and label.
 */
public class StatCard extends RoundedPanel {
    public StatCard(String icon, String value, String label, Color accentColor) {
        super(ModernTheme.CARD_RADIUS, ModernTheme.CARD_BG);
        setLayout(new BorderLayout());
        setShowShadow(true);

        // Left accent bar
        JPanel accentBar = new JPanel();
        accentBar.setBackground(accentColor);
        accentBar.setPreferredSize(new Dimension(5, 0));
        add(accentBar, BorderLayout.WEST);

        // Content
        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 16, 4, 16);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconLbl.setForeground(accentColor);
        gbc.gridx = 0; gbc.gridy = 0;
        content.add(iconLbl, gbc);

        JLabel valueLbl = new JLabel(value);
        valueLbl.setFont(ModernTheme.FONT_HEADING);
        valueLbl.setForeground(ModernTheme.TEXT_PRIMARY);
        gbc.gridy = 1;
        content.add(valueLbl, gbc);

        JLabel labelLbl = new JLabel(label);
        labelLbl.setFont(ModernTheme.FONT_SMALL);
        labelLbl.setForeground(ModernTheme.TEXT_SECONDARY);
        gbc.gridy = 2;
        content.add(labelLbl, gbc);

        add(content, BorderLayout.CENTER);
    }
}
