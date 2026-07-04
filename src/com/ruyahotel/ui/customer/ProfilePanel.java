package com.ruyahotel.ui.customer;

import com.ruyahotel.model.User;
import com.ruyahotel.service.UserService;
import com.ruyahotel.service.ValidationService;
import com.ruyahotel.ui.components.*;
import com.ruyahotel.util.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class ProfilePanel extends JPanel {
    private final Consumer<String> navigator;
    private final UserService userService = new UserService();
    private RoundedTextField firstField, lastField, emailField, phoneField, addressField, natField;
    private JComboBox<String> genderBox;
    private JPasswordField currentPass, newPass, confirmPass;
    private JLabel msgLabel;

    public ProfilePanel(Consumer<String> navigator) {
        this.navigator = navigator;
        setLayout(new BorderLayout());
        setBackground(ModernTheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(ModernTheme.FONT_TITLE);
        tabs.setBackground(ModernTheme.CARD_BG);
        tabs.setForeground(ModernTheme.TEXT_PRIMARY);

        tabs.addTab("Profile Info", buildProfileTab());
        tabs.addTab("Change Password", buildPasswordTab());

        add(tabs, BorderLayout.CENTER);
        loadProfile();
    }

    private JPanel buildProfileTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ModernTheme.BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; panel.add(fieldLabel("First Name"), gbc);
        gbc.gridx = 1; firstField = new RoundedTextField(20); panel.add(firstField, gbc);

        gbc.gridy = 1; gbc.gridx = 0; panel.add(fieldLabel("Last Name"), gbc);
        gbc.gridx = 1; lastField = new RoundedTextField(20); panel.add(lastField, gbc);

        gbc.gridy = 2; gbc.gridx = 0; panel.add(fieldLabel("Email"), gbc);
        gbc.gridx = 1; emailField = new RoundedTextField(20); panel.add(emailField, gbc);
        emailField.setEditable(true);
        emailField.setBackground(Color.WHITE);

        gbc.gridy = 3; gbc.gridx = 0; panel.add(fieldLabel("Phone"), gbc);
        gbc.gridx = 1; phoneField = new RoundedTextField(20); panel.add(phoneField, gbc);

        gbc.gridy = 4; gbc.gridx = 0; panel.add(fieldLabel("Gender"), gbc);
        gbc.gridx = 1; genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        genderBox.setFont(ModernTheme.FONT_BODY); panel.add(genderBox, gbc);

        gbc.gridy = 5; gbc.gridx = 0; panel.add(fieldLabel("Nationality"), gbc);
        gbc.gridx = 1; natField = new RoundedTextField(20); panel.add(natField, gbc);

        gbc.gridy = 6; gbc.gridx = 0; panel.add(fieldLabel("Address"), gbc);
        gbc.gridx = 1; addressField = new RoundedTextField(20); panel.add(addressField, gbc);

        gbc.gridy = 7; gbc.gridx = 1;
        msgLabel = new JLabel(" ");
        msgLabel.setFont(ModernTheme.FONT_SMALL);
        msgLabel.setForeground(ModernTheme.SUCCESS);
        panel.add(msgLabel, gbc);

        gbc.gridy = 8; gbc.gridx = 1;
        RoundedButton saveBtn = new RoundedButton("Save Changes", ModernTheme.PRIMARY);
        saveBtn.setPreferredSize(new Dimension(160, 44));
        saveBtn.addActionListener(e -> saveProfile());
        panel.add(saveBtn, gbc);

        return panel;
    }

    private JPanel buildPasswordTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ModernTheme.BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridy = 0; gbc.gridx = 0; panel.add(fieldLabel("Current Password"), gbc);
        gbc.gridx = 1; currentPass = new JPasswordField(20); panel.add(currentPass, gbc);

        gbc.gridy = 1; gbc.gridx = 0; panel.add(fieldLabel("New Password"), gbc);
        gbc.gridx = 1; newPass = new JPasswordField(20); panel.add(newPass, gbc);

        gbc.gridy = 2; gbc.gridx = 0; panel.add(fieldLabel("Confirm Password"), gbc);
        gbc.gridx = 1; confirmPass = new JPasswordField(20); panel.add(confirmPass, gbc);

        gbc.gridy = 3; gbc.gridx = 1;
        RoundedButton changeBtn = new RoundedButton("Change Password", ModernTheme.PRIMARY);
        changeBtn.setPreferredSize(new Dimension(180, 44));
        changeBtn.addActionListener(e -> changePassword());
        panel.add(changeBtn, gbc);

        return panel;
    }

    private void loadProfile() {
        User u = SessionManager.getInstance().getCurrentUser();
        if (u == null) return;
        firstField.setText(u.getFirstName());
        lastField.setText(u.getLastName());
        emailField.setText(u.getEmail());
        phoneField.setText(u.getPhone());
        genderBox.setSelectedItem(u.getGender());
        natField.setText(u.getNationality());
        addressField.setText(u.getAddress() != null ? u.getAddress() : "");
    }

    private void saveProfile() {
        User u = SessionManager.getInstance().getCurrentUser();
        if (u == null) return;
        u.setFirstName(firstField.getText().trim());
        u.setLastName(lastField.getText().trim());
        u.setPhone(phoneField.getText().trim());
        u.setGender((String) genderBox.getSelectedItem());
        u.setNationality(natField.getText().trim());
        u.setAddress(addressField.getText().trim());

        String newEmail = emailField.getText().trim();
        if (!newEmail.equals(u.getEmail()) && !ValidationService.isValidEmail(newEmail)) {
            msgLabel.setText("Invalid email format.");
            msgLabel.setForeground(ModernTheme.DANGER);
            return;
        }
        u.setEmail(newEmail);

        if (userService.updateProfile(u)) {
            msgLabel.setText("Profile updated successfully!");
            msgLabel.setForeground(ModernTheme.SUCCESS);
        } else {
            msgLabel.setText("Update failed.");
            msgLabel.setForeground(ModernTheme.DANGER);
        }
    }

    private void changePassword() {
        User u = SessionManager.getInstance().getCurrentUser();
        if (u == null) return;
        String cur = new String(currentPass.getPassword());
        String nw = new String(newPass.getPassword());
        String cf = new String(confirmPass.getPassword());

        if (userService.changePassword(u.getUserId(), cur, nw, cf)) {
            JOptionPane.showMessageDialog(this, "Password changed successfully!");
            currentPass.setText(""); newPass.setText(""); confirmPass.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Password change failed. Check current password and rules.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(ModernTheme.FONT_TITLE);
        l.setForeground(ModernTheme.TEXT_PRIMARY);
        return l;
    }
}
