package com.ruyahotel.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Modern rounded button with hover and click animations.
 */
public class RoundedButton extends JButton {
    private int radius = 10;
    private Color normalColor = ModernTheme.PRIMARY;
    private Color hoverColor = ModernTheme.PRIMARY_DARK;
    private Color pressColor = ModernTheme.PRIMARY_LIGHT;
    private Color currentColor = normalColor;
    private boolean isGradient = false;

    public RoundedButton(String text) {
        super(text);
        setup();
    }

    public RoundedButton(String text, Color color) {
        super(text);
        this.normalColor = color;
        this.hoverColor = color.darker();
        this.pressColor = color.brighter();
        this.currentColor = color;
        setup();
    }

    public RoundedButton(String text, Color normal, Color hover) {
        super(text);
        this.normalColor = normal;
        this.hoverColor = hover;
        this.pressColor = hover.brighter();
        this.currentColor = normal;
        setup();
    }

    private void setup() {
        setOpaque(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setFont(ModernTheme.FONT_BUTTON);
        setForeground(ModernTheme.TEXT_ON_PRIMARY);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                currentColor = hoverColor;
                repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                currentColor = normalColor;
                repaint();
            }
            @Override
            public void mousePressed(MouseEvent e) {
                currentColor = pressColor;
                repaint();
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                currentColor = hoverColor;
                repaint();
            }
        });
    }

    public void setRadius(int r) {
        this.radius = r;
        repaint();
    }

    public void setGradient(boolean gradient) {
        this.isGradient = gradient;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (isGradient) {
            g2.setPaint(new GradientPaint(0, 0, currentColor, getWidth(), getHeight(), currentColor.darker()));
        } else {
            g2.setColor(currentColor);
        }

        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        // Draw text
        g2.setColor(getForeground());
        g2.setFont(getFont());
        FontMetrics fm = g2.getFontMetrics();
        String text = getText();
        int x = (getWidth() - fm.stringWidth(text)) / 2;
        int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
        g2.drawString(text, x, y);

        g2.dispose();
    }
}
