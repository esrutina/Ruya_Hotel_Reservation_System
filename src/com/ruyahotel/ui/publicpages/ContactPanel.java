package com.ruyahotel.ui.publicpages;

import com.ruyahotel.dao.SettingsDAO;
import com.ruyahotel.ui.components.*;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class ContactPanel extends JPanel {
    private final SettingsDAO settingsDAO = new SettingsDAO();

    public ContactPanel(Consumer<String> navigator) {
        setLayout(new BorderLayout());
        setBackground(ModernTheme.BACKGROUND);

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(ModernTheme.BACKGROUND);
        content.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Contact Us");
        title.setFont(ModernTheme.FONT_HEADING);
        title.setForeground(ModernTheme.TEXT_PRIMARY);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        content.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        content.add(label("Name"), gbc);
        gbc.gridx = 1;
        RoundedTextField nameField = new RoundedTextField("Your full name");
        nameField.setPreferredSize(new Dimension(300, 44));
        content.add(nameField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        content.add(label("Email"), gbc);
        gbc.gridx = 1;
        RoundedTextField emailField = new RoundedTextField("your@email.com");
        emailField.setPreferredSize(new Dimension(300, 44));
        content.add(emailField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        content.add(label("Message"), gbc);
        gbc.gridx = 1;
        JTextArea msgArea = new JTextArea(5, 30);
        msgArea.setFont(ModernTheme.FONT_BODY);
        msgArea.setLineWrap(true);
        msgArea.setWrapStyleWord(true);
        msgArea.setBorder(BorderFactory.createCompoundBorder(
                new javax.swing.border.LineBorder(ModernTheme.BORDER, 1, true),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));
        JScrollPane scroll = new JScrollPane(msgArea);
        scroll.setBorder(null);
        content.add(scroll, gbc);

        gbc.gridy = 4;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        RoundedButton sendBtn = new RoundedButton("Send Message", ModernTheme.PRIMARY);
        sendBtn.setPreferredSize(new Dimension(160, 44));
        sendBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Thank you! We will get back to you soon.",
                    "Message Sent", JOptionPane.INFORMATION_MESSAGE);
            nameField.setText("");
            emailField.setText("");
            msgArea.setText("");
        });
        content.add(sendBtn, gbc);

        gbc.gridy = 5;
        gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(40, 12, 12, 12);
        JPanel info = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        info.setOpaque(false);
        info.add(infoLabel("\uD83D\uDCDE", settingsDAO.getValue("contact_phone") != null ? settingsDAO.getValue("contact_phone") : "+251 911 000 000"));
        info.add(infoLabel("\uD83D\uDCE7", settingsDAO.getValue("contact_email") != null ? settingsDAO.getValue("contact_email") : "info@ruyahotel.com"));
        info.add(infoLabel("\uD83D\uDCCD", settingsDAO.getValue("hotel_address") != null ? settingsDAO.getValue("hotel_address") : "Addis Ababa, Ethiopia"));
        content.add(info, gbc);

        add(content, BorderLayout.CENTER);
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(ModernTheme.FONT_TITLE);
        l.setForeground(ModernTheme.TEXT_PRIMARY);
        return l;
    }

    private JLabel infoLabel(String icon, String text) {
        JLabel l = new JLabel(icon + "  " + text);
        l.setFont(ModernTheme.FONT_BODY);
        l.setForeground(ModernTheme.TEXT_SECONDARY);
        return l;
    }
}
