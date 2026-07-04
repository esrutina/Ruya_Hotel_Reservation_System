package com.ruyahotel.ui.admin;

import com.ruyahotel.model.Feedback;
import com.ruyahotel.service.FeedbackService;
import com.ruyahotel.ui.components.ModernTable;
import com.ruyahotel.ui.components.ModernTheme;
import com.ruyahotel.ui.components.RoundedButton;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class FeedbackManagementPanel extends JPanel {
    private final Consumer<String> navigator;
    private final FeedbackService feedbackService = new FeedbackService();
    private ModernTable table;
    private DefaultTableModel model;

    public FeedbackManagementPanel(Consumer<String> navigator) {
        this.navigator = navigator;
        setLayout(new BorderLayout());
        setBackground(ModernTheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("Feedback Management");
        title.setFont(ModernTheme.FONT_HEADING);
        title.setForeground(ModernTheme.TEXT_PRIMARY);
        add(title, BorderLayout.NORTH);

        JLabel avgLabel = new JLabel("Average Rating: " + String.format("%.1f", feedbackService.getAverageRating()) + " / 5");
        avgLabel.setFont(ModernTheme.FONT_SUBHEADING);
        avgLabel.setForeground(ModernTheme.ACCENT);
        add(avgLabel, BorderLayout.SOUTH);

        String[] cols = {"ID", "Customer", "Room", "Rating", "Comment", "Date"};
        model = new DefaultTableModel(cols, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        table = new ModernTable(model);
        add(table.wrapInScrollPane(), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.setOpaque(false);
        RoundedButton delBtn = new RoundedButton("Delete Selected", ModernTheme.DANGER);
        delBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int fid = (int) model.getValueAt(row, 0);
                feedbackService.deleteFeedback(fid);
                JOptionPane.showMessageDialog(this, "Feedback deleted.");
                loadFeedback();
            }
        });
        bottom.add(delBtn);
        RoundedButton refreshBtn = new RoundedButton("Refresh", ModernTheme.INFO);
        refreshBtn.addActionListener(e -> loadFeedback());
        bottom.add(refreshBtn);
        add(bottom, BorderLayout.SOUTH);
        loadFeedback();
    }

    private void loadFeedback() {
        model.setRowCount(0);
        List<Feedback> list = feedbackService.getAll();
        for (Feedback f : list) {
            model.addRow(new Object[]{
                    f.getFeedbackId(), f.getCustomerName(), f.getRoomType(),
                    f.getStarDisplay(), f.getComment(),
                    f.getCreatedAt() != null ? f.getCreatedAt().toLocalDate().toString() : ""
            });
        }
    }
}
