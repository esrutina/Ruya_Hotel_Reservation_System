package com.ruyahotel.ui.components;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

/**
 * Modern styled JTable with custom header and alternating row colors.
 */
public class ModernTable extends JTable {

    public ModernTable(DefaultTableModel model) {
        super(model);
        setup();
    }

    public ModernTable(Object[][] data, String[] columns) {
        super(data, columns);
        setup();
    }

    private void setup() {
        setRowHeight(44);
        setFont(ModernTheme.FONT_BODY);
        setForeground(ModernTheme.TEXT_PRIMARY);
        setSelectionBackground(ModernTheme.PRIMARY_LIGHT);
        setSelectionForeground(ModernTheme.TEXT_ON_PRIMARY);
        setGridColor(ModernTheme.BORDER_LIGHT);
        setShowGrid(false);
        setIntercellSpacing(new Dimension(0, 0));
        setFillsViewportHeight(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Header styling
        JTableHeader header = getTableHeader();
        header.setFont(ModernTheme.FONT_TITLE);
        header.setPreferredSize(new Dimension(0, 44));
        header.setReorderingAllowed(false);

        // Custom Header Renderer to fix Windows L&F empty/white header bug
        header.setDefaultRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                JLabel label = new JLabel(value != null ? value.toString() : "");
                label.setFont(ModernTheme.FONT_TITLE);
                label.setForeground(ModernTheme.TEXT_ON_PRIMARY);
                label.setBackground(ModernTheme.PRIMARY);
                label.setOpaque(true);

                // Alignment based on column
                if (column == 0 || column == 1 || column == 4 || column == 5) {
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    label.setHorizontalAlignment(SwingConstants.LEFT);
                }

                label.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, ModernTheme.BORDER_LIGHT),
                        BorderFactory.createEmptyBorder(8, 16, 8, 16)
                ));
                return label;
            }
        });
    }

    private final TableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Set cell padding and font
            setFont(ModernTheme.FONT_BODY);
            setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

            // Setup alignment
            if (column == 0 || column == 1 || column == 4 || column == 5) {
                setHorizontalAlignment(SwingConstants.CENTER);
            } else {
                setHorizontalAlignment(SwingConstants.LEFT);
            }

            if (isSelected) {
                c.setBackground(ModernTheme.PRIMARY_LIGHT);
                c.setForeground(ModernTheme.TEXT_ON_PRIMARY);
            } else {
                c.setBackground(row % 2 == 0 ? ModernTheme.CARD_BG : ModernTheme.BACKGROUND);
                c.setForeground(ModernTheme.TEXT_PRIMARY);
            }
            return c;
        }
    };

    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        return cellRenderer;
    }

    public JScrollPane wrapInScrollPane() {
        JScrollPane scroll = new JScrollPane(this);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(ModernTheme.BACKGROUND);
        return scroll;
    }
}
