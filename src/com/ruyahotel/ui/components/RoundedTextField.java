package com.ruyahotel.ui.components;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Modern rounded text field with placeholder text support.
 */
public class RoundedTextField extends JTextField {
    private String placeholder = "";
    private int radius = 10;
    private Color borderColor = ModernTheme.BORDER;
    private Color focusColor = ModernTheme.PRIMARY;
    private Color currentBorder = borderColor;

    public RoundedTextField(int columns) {
        super(columns);
        setup();
    }

    public RoundedTextField(String text, int columns) {
        super(text, columns);
        setup();
    }

    public RoundedTextField(String placeholder) {
        super(20);
        this.placeholder = placeholder;
        setup();
    }

    private void setup() {
        setOpaque(false);
        setFont(ModernTheme.FONT_BODY);
        setForeground(ModernTheme.TEXT_PRIMARY);
        setCaretColor(ModernTheme.PRIMARY);
        setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

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
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        // Border
        g2.setColor(currentBorder);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

        g2.dispose();

        super.paintComponent(g);

        // Placeholder text
        if (getText().isEmpty() && !placeholder.isEmpty() && !hasFocus()) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(ModernTheme.TEXT_MUTED);
            g2d.setFont(getFont());
            Insets insets = getInsets();
            g2d.drawString(placeholder, insets.left + 4, (getHeight() + g2d.getFontMetrics().getAscent() - g2d.getFontMetrics().getDescent()) / 2);
            g2d.dispose();
        }
    }
}
