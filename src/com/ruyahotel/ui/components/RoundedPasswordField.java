package com.ruyahotel.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Modern rounded password field with show/hide toggle.
 */
public class RoundedPasswordField extends JPasswordField {
    private int radius = 10;
    private Color borderColor = ModernTheme.BORDER;
    private Color focusColor = ModernTheme.PRIMARY;
    private Color currentBorder = borderColor;
    private JToggleButton toggleBtn;
    private JPanel wrapper;

    public RoundedPasswordField(int columns) {
        super(columns);
        setup();
    }

    private void setup() {
        setOpaque(false);
        setFont(ModernTheme.FONT_BODY);
        setForeground(ModernTheme.TEXT_PRIMARY);
        setCaretColor(ModernTheme.PRIMARY);
        setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 40));
        setEchoChar('\u2022');

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                currentBorder = focusColor;
                repaint();
            }
            @Override
            public void focusLost(FocusEvent e) {
                currentBorder = borderColor;
                repaint();
            }
        });

        // Show/Hide toggle (eye icon simulated with text)
        toggleBtn = new JToggleButton("\uD83D\uDC41");
        toggleBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        toggleBtn.setOpaque(false);
        toggleBtn.setContentAreaFilled(false);
        toggleBtn.setBorderPainted(false);
        toggleBtn.setFocusPainted(false);
        toggleBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toggleBtn.setToolTipText("Show/Hide Password");

        toggleBtn.addActionListener(e -> {
            if (toggleBtn.isSelected()) {
                setEchoChar((char) 0);
                toggleBtn.setText("\uD83D\uDC41\u200D\uD83D\uDDE8");
            } else {
                setEchoChar('\u2022');
                toggleBtn.setText("\uD83D\uDC41");
            }
        });

        // We will not add toggle here; the parent layout should handle it
        // Instead provide accessor
    }

    public JToggleButton getToggleButton() {
        return toggleBtn;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        g2.setColor(currentBorder);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

        g2.dispose();
        super.paintComponent(g);
    }
}
