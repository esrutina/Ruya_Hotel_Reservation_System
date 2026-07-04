package com.ruyahotel.ui.components;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Modern UI Theme for Ruya Hotel Management System.
 * Provides a cohesive, beautiful color palette and styling without external L&F.
 */
public class ModernTheme {

    // Brand Colors
    public static final Color PRIMARY = new Color(26, 86, 219);       // Deep Blue
    public static final Color PRIMARY_DARK = new Color(20, 65, 165);
    public static final Color PRIMARY_LIGHT = new Color(59, 130, 246);
    public static final Color ACCENT = new Color(245, 158, 11);     // Amber/Gold
    public static final Color ACCENT_HOVER = new Color(217, 119, 6);

    // Backgrounds
    public static final Color BACKGROUND = new Color(248, 250, 252);
    public static final Color CARD_BG = new Color(255, 255, 255);
    public static final Color SIDEBAR_BG = new Color(17, 24, 39);
    public static final Color SIDEBAR_HOVER = new Color(31, 41, 55);
    public static final Color SIDEBAR_ACTIVE = new Color(26, 86, 219);

    // Text Colors
    public static final Color TEXT_PRIMARY = new Color(17, 24, 39);
    public static final Color TEXT_SECONDARY = new Color(107, 114, 128);
    public static final Color TEXT_MUTED = new Color(156, 163, 175);
    public static final Color TEXT_ON_PRIMARY = Color.WHITE;
    public static final Color TEXT_ON_SIDEBAR = new Color(209, 213, 219);

    // Status Colors
    public static final Color SUCCESS = new Color(16, 185, 129);
    public static final Color SUCCESS_LIGHT = new Color(209, 250, 229);
    public static final Color WARNING = new Color(245, 158, 11);
    public static final Color WARNING_LIGHT = new Color(254, 243, 199);
    public static final Color DANGER = new Color(239, 68, 68);
    public static final Color DANGER_LIGHT = new Color(254, 226, 226);
    public static final Color INFO = new Color(59, 130, 246);
    public static final Color INFO_LIGHT = new Color(219, 234, 254);

    // Borders & Shadows
    public static final Color BORDER = new Color(229, 231, 235);
    public static final Color BORDER_LIGHT = new Color(243, 244, 246);
    public static final int BORDER_RADIUS = 12;
    public static final int CARD_RADIUS = 16;

    // Fonts
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_SUBHEADING = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 13);

    // Apply global look improvements
    public static void setup() {
        UIManager.put("Panel.background", BACKGROUND);
        UIManager.put("OptionPane.background", BACKGROUND);
        UIManager.put("ScrollPane.background", BACKGROUND);
        UIManager.put("Table.background", CARD_BG);
        UIManager.put("Table.gridColor", BORDER);
        UIManager.put("TableHeader.background", PRIMARY);
        UIManager.put("TableHeader.foreground", TEXT_ON_PRIMARY);
        UIManager.put("TableHeader.font", FONT_TITLE);
        UIManager.put("Table.selectionBackground", PRIMARY_LIGHT);
        UIManager.put("Table.selectionForeground", TEXT_ON_PRIMARY);
        UIManager.put("ComboBox.background", CARD_BG);
        UIManager.put("ComboBox.selectionBackground", PRIMARY_LIGHT);
        UIManager.put("TextField.caretForeground", PRIMARY);
    }

    public static Border cardBorder() {
        return BorderFactory.createCompoundBorder(
                new RoundedBorder(CARD_RADIUS, new Color(0, 0, 0, 15)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );
    }

    public static Border panelBorder() {
        return BorderFactory.createCompoundBorder(
                new RoundedBorder(BORDER_RADIUS, BORDER),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        );
    }

    public static Border inputBorder() {
        return BorderFactory.createCompoundBorder(
                new RoundedBorder(8, BORDER),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)
        );
    }

    public static Border emptyBorder(int size) {
        return BorderFactory.createEmptyBorder(size, size, size, size);
    }

    // Helper: create a gradient paint
    public static Paint primaryGradient(int height) {
        return new GradientPaint(0, 0, PRIMARY, 0, height, PRIMARY_DARK);
    }
}

/**
 * Custom rounded border with optional shadow-like stroke
 */
class RoundedBorder implements Border {
    private final int radius;
    private final Color color;

    public RoundedBorder(int radius, Color color) {
        this.radius = radius;
        this.color = color;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(radius / 3, radius, radius / 3, radius);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        g2.dispose();
    }
}
