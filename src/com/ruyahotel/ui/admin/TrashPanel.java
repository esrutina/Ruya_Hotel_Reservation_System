package com.ruyahotel.ui.admin;

import com.ruyahotel.dao.TrashDAO;
import com.ruyahotel.dao.UserDAO;
import com.ruyahotel.ui.components.ModernTable;
import com.ruyahotel.ui.components.ModernTheme;
import com.ruyahotel.ui.components.RoundedButton;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class TrashPanel extends JPanel {
    private final Consumer<String> navigator;
    private final TrashDAO trashDAO = new TrashDAO();
    private final UserDAO userDAO = new UserDAO();
    private ModernTable table;
    private DefaultTableModel model;

    public TrashPanel(Consumer<String> navigator) {
        this.navigator = navigator;
        setLayout(new BorderLayout());
        setBackground(ModernTheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("Trash / Recycle Bin");
        title.setFont(ModernTheme.FONT_HEADING);
        title.setForeground(ModernTheme.TEXT_PRIMARY);
        add(title, BorderLayout.NORTH);

        String[] cols = {"Trash ID", "Type", "Name", "Deleted Date", "Deleted By", "Original ID"};
        model = new DefaultTableModel(cols, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        table = new ModernTable(model);
        add(table.wrapInScrollPane(), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.setOpaque(false);
        RoundedButton restoreBtn = new RoundedButton("Restore", ModernTheme.SUCCESS);
        restoreBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) return;
            int trashId = (int) model.getValueAt(row, 0);
            String itemType = (String) model.getValueAt(row, 1);
            int originalId = (int) model.getValueAt(row, 5);

            if ("User".equals(itemType)) {
                if (userDAO.restoreFromTrash(originalId)) {
                    trashDAO.restore(trashId);
                    JOptionPane.showMessageDialog(this, "User restored successfully!");
                    loadTrash();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to restore user.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                if (trashDAO.restore(trashId)) {
                    JOptionPane.showMessageDialog(this, "Item marked as restored.");
                    loadTrash();
                }
            }
        });
        bottom.add(restoreBtn);

        RoundedButton permBtn = new RoundedButton("Delete Permanently", ModernTheme.DANGER);
        permBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int tid = (int) model.getValueAt(row, 0);
                trashDAO.remove(tid);
                loadTrash();
            }
        });
        bottom.add(permBtn);

        RoundedButton refreshBtn = new RoundedButton("Refresh", ModernTheme.INFO);
        refreshBtn.addActionListener(e -> loadTrash());
        bottom.add(refreshBtn);

        add(bottom, BorderLayout.SOUTH);
        loadTrash();
    }

    private void loadTrash() {
        model.setRowCount(0);
        List<Object[]> list = trashDAO.getAll();
        for (Object[] row : list) {
            model.addRow(new Object[]{row[0], row[1], row[2], row[3], row[4], row[5]});
        }
    }
}
