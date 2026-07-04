package com.ruyahotel.ui.components;

import javax.swing.*;
import java.awt.*;

/**
 * Custom chart panel supporting bar and pie charts using Java2D.
 */
public class ChartPanel extends JPanel {
    public enum ChartType { BAR, PIE }

    private ChartType type = ChartType.BAR;
    private String[] labels;
    private double[] values;
    private Color[] colors;
    private String title = "";

    public ChartPanel() {
        setOpaque(false);
        setPreferredSize(new Dimension(400, 250));
    }

    public void setBarData(String[] labels, double[] values, Color[] colors, String title) {
        this.type = ChartType.BAR;
        this.labels = labels;
        this.values = values;
        this.colors = colors;
        this.title = title;
        repaint();
    }

    public void setPieData(String[] labels, double[] values, Color[] colors, String title) {
        this.type = ChartType.PIE;
        this.labels = labels;
        this.values = values;
        this.colors = colors;
        this.title = title;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (type == ChartType.BAR) {
            drawBarChart(g2);
        } else {
            drawPieChart(g2);
        }

        g2.dispose();
    }

    private void drawBarChart(Graphics2D g2) {
        int pad = 40;
        int w = getWidth() - pad * 2;
        int h = getHeight() - pad * 2 - 30;
        int x0 = pad;
        int y0 = getHeight() - pad - 30;

        if (labels == null || values == null || labels.length == 0) return;

        double max = 0;
        for (double v : values) if (v > max) max = v;
        if (max == 0) max = 1;

        int barWidth = w / values.length - 16;

        // Title
        g2.setFont(ModernTheme.FONT_SUBHEADING);
        g2.setColor(ModernTheme.TEXT_PRIMARY);
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(title, (getWidth() - fm.stringWidth(title)) / 2, pad - 10);

        // Draw bars
        for (int i = 0; i < values.length; i++) {
            int barH = (int) ((values[i] / max) * h);
            int x = x0 + i * (barWidth + 16) + 8;
            int y = y0 - barH;

            g2.setColor(colors != null && i < colors.length ? colors[i] : ModernTheme.PRIMARY);
            g2.fillRoundRect(x, y, barWidth, barH, 6, 6);

            // Label
            g2.setFont(ModernTheme.FONT_SMALL);
            g2.setColor(ModernTheme.TEXT_SECONDARY);
            g2.drawString(labels[i], x + barWidth / 2 - g2.getFontMetrics().stringWidth(labels[i]) / 2, y0 + 20);

            // Value
            g2.setColor(ModernTheme.TEXT_PRIMARY);
            g2.drawString(String.valueOf((int) values[i]), x + barWidth / 2 - g2.getFontMetrics().stringWidth(String.valueOf((int) values[i])) / 2, y - 6);
        }

        // Base line
        g2.setColor(ModernTheme.BORDER);
        g2.drawLine(x0, y0, x0 + w, y0);
    }

    private void drawPieChart(Graphics2D g2) {
        int pad = 40;
        int diameter = Math.min(getWidth(), getHeight()) - pad * 2 - 60;
        int x = (getWidth() - diameter) / 2;
        int y = (getHeight() - diameter) / 2 + 10;

        if (labels == null || values == null || labels.length == 0) return;

        double total = 0;
        for (double v : values) total += v;
        if (total == 0) total = 1;

        // Title
        g2.setFont(ModernTheme.FONT_SUBHEADING);
        g2.setColor(ModernTheme.TEXT_PRIMARY);
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(title, (getWidth() - fm.stringWidth(title)) / 2, pad - 10);

        double startAngle = 0;
        for (int i = 0; i < values.length; i++) {
            double arc = (values[i] / total) * 360;
            g2.setColor(colors != null && i < colors.length ? colors[i] : ModernTheme.PRIMARY);
            g2.fillArc(x, y, diameter, diameter, (int) startAngle, (int) arc);
            startAngle += arc;
        }

        // Legend on right side
        int lx = x + diameter + 20;
        int ly = y;
        g2.setFont(ModernTheme.FONT_SMALL);
        for (int i = 0; i < labels.length; i++) {
            g2.setColor(colors != null && i < colors.length ? colors[i] : ModernTheme.PRIMARY);
            g2.fillRoundRect(lx, ly + i * 22, 12, 12, 4, 4);
            g2.setColor(ModernTheme.TEXT_SECONDARY);
            String txt = labels[i] + " (" + String.format("%.1f%%", (values[i] / total) * 100) + ")";
            g2.drawString(txt, lx + 18, ly + i * 22 + 11);
        }
    }
}
