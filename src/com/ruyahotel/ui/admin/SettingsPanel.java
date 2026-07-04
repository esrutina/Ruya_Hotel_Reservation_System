package com.ruyahotel.ui.admin;

import com.ruyahotel.model.Setting;
import com.ruyahotel.service.UserService;
import com.ruyahotel.ui.components.*;
import com.ruyahotel.util.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class SettingsPanel extends JPanel {
    private final Consumer<String> navigator;
    private final com.ruyahotel.dao.SettingsDAO settingsDAO = new com.ruyahotel.dao.SettingsDAO();

    public SettingsPanel(Consumer<String> navigator) {
        this.navigator = navigator;
        setLayout(new BorderLayout());
        setBackground(ModernTheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("System Settings");
        title.setFont(ModernTheme.FONT_HEADING);
        title.setForeground(ModernTheme.TEXT_PRIMARY);
        add(title, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(ModernTheme.FONT_TITLE);
        tabs.addTab("Hotel Info", categoryPanel("Hotel"));
        tabs.addTab("Business Rules", categoryPanel("Business"));
        tabs.addTab("Room", categoryPanel("Room"));
        tabs.addTab("User", categoryPanel("User"));
        // Removed About and Contact tabs as requested

        add(tabs, BorderLayout.CENTER);
    }

    private JPanel categoryPanel(String category) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ModernTheme.BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        List<Setting> settings = settingsDAO.getByCategory(category);
        int row = 0;
        for (Setting s : settings) {
            gbc.gridy = row; gbc.gridx = 0;
            JLabel label = new JLabel(formatKey(s.getSettingKey()));
            label.setFont(ModernTheme.FONT_TITLE);
            label.setForeground(ModernTheme.TEXT_PRIMARY);
            panel.add(label, gbc);

            gbc.gridx = 1;
            RoundedTextField field = new RoundedTextField(s.getSettingValue());
            field.setName(s.getSettingKey());
            field.setPreferredSize(new Dimension(400, 40));
            panel.add(field, gbc);
            row++;
        }

        gbc.gridy = row; gbc.gridx = 1;
        RoundedButton saveBtn = new RoundedButton("Save Changes", ModernTheme.PRIMARY);
        saveBtn.setPreferredSize(new Dimension(160, 44));
        saveBtn.addActionListener(e -> {
            for (Component c : panel.getComponents()) {
                if (c instanceof RoundedTextField) {
                    RoundedTextField f = (RoundedTextField) c;
                    settingsDAO.setValue(f.getName(), f.getText().trim());
                }
            }
            JOptionPane.showMessageDialog(panel, "Settings saved!");
        });
        panel.add(saveBtn, gbc);

        return panel;
    }

    private String formatKey(String key) {
        if (key == null || key.isEmpty()) return "";
        return key.replace("_", " ").substring(0, 1).toUpperCase() + key.replace("_", " ").substring(1);
    }
}
