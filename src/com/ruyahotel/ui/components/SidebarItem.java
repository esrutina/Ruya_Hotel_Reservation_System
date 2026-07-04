package com.ruyahotel.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * A sidebar navigation item with icon, label, and hover/active states.
 */
public class SidebarItem extends JPanel {
    private final String label;
    private final String icon;
    private boolean active = false;
    private Runnable onClick;

    public SidebarItem(String icon, String label) {
        this.label = label;
        this.icon = icon;
        setup();
    }

    private void setup() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 12, 0));
        setBackground(ModernTheme.SIDEBAR_BG);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        setPreferredSize(new Dimension(240, 48));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        iconLbl.setForeground(ModernTheme.TEXT_ON_SIDEBAR);

        JLabel textLbl = new JLabel(label);
        textLbl.setFont(ModernTheme.FONT_BODY);
        textLbl.setForeground(ModernTheme.TEXT_ON_SIDEBAR);

        add(iconLbl);
        add(textLbl);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!active) setBackground(ModernTheme.SIDEBAR_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (!active) setBackground(ModernTheme.SIDEBAR_BG);
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                if (onClick != null) onClick.run();
            }
        });
    }

    public void setActive(boolean active) {
        this.active = active;
        setBackground(active ? ModernTheme.SIDEBAR_ACTIVE : ModernTheme.SIDEBAR_BG);
        repaint();
    }

    public void onClick(Runnable action) {
        this.onClick = action;
    }
}
