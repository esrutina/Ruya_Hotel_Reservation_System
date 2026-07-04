package com.ruyahotel.ui.components;

import javax.swing.*;
import java.awt.*;

/**
 * A JPanel with rounded corners, optional shadow, and gradient background support.
 */
public class RoundedPanel extends JPanel {
    private int radius = ModernTheme.CARD_RADIUS;
    private Color bgColor = ModernTheme.CARD_BG;
    private Color shadowColor = new Color(0, 0, 0, 20);
    private int shadowSize = 4;
    private boolean showShadow = true;
    private Paint customPaint = null;

    public RoundedPanel() {
        super();
        setOpaque(false);
        setBackground(bgColor);
        setLayout(new BorderLayout());
    }

    public RoundedPanel(LayoutManager layout) {
        super(layout);
        setOpaque(false);
        setBackground(bgColor);
    }

    public RoundedPanel(int radius) {
        this();
        this.radius = radius;
    }

    public RoundedPanel(int radius, Color bgColor) {
        this();
        this.radius = radius;
        this.bgColor = bgColor;
    }

    public void setShowShadow(boolean show) {
        this.showShadow = show;
        repaint();
    }

    public void setShadowSize(int size) {
        this.shadowSize = size;
        repaint();
    }

    public void setCustomPaint(Paint paint) {
        this.customPaint = paint;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // Draw shadow
        if (showShadow) {
            g2.setColor(shadowColor);
            g2.fillRoundRect(shadowSize, shadowSize, w - shadowSize * 2, h - shadowSize * 2, radius, radius);
        }

        // Draw background
        if (customPaint != null) {
            g2.setPaint(customPaint);
        } else {
            g2.setColor(bgColor);
        }
        g2.fillRoundRect(0, 0, w - shadowSize, h - shadowSize, radius, radius);

        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    public Insets getInsets() {
        return new Insets(16, 16, 16 + shadowSize, 16 + shadowSize);
    }
}
