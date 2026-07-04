package com.ruyahotel.ui.publicpages;

import com.ruyahotel.model.User;
import com.ruyahotel.service.AuthService;
import com.ruyahotel.service.ValidationService;
import com.ruyahotel.ui.components.*;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class RegisterPanel extends JPanel {
    private final Consumer<String> navigator;
    private final AuthService authService = new AuthService();

    private RoundedTextField firstField, lastField, userField, emailField, phoneField, natField;
    private RoundedPasswordField passField, confirmField;
    private JComboBox<String> genderBox;
    private JLabel errorLabel;

    public RegisterPanel(Consumer<String> navigator) {
        this.navigator = navigator;
        setLayout(new BorderLayout());
        setBackground(ModernTheme.BACKGROUND);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(ModernTheme.BACKGROUND);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 12, 6, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Create Account");
        title.setFont(ModernTheme.FONT_HEADING);
        title.setForeground(ModernTheme.TEXT_PRIMARY);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        formPanel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0; formPanel.add(fieldLabel("First Name"), gbc);
        gbc.gridx = 1; firstField = new RoundedTextField("John"); formPanel.add(firstField, gbc);
        gbc.gridx = 2; formPanel.add(fieldLabel("Last Name"), gbc);
        gbc.gridx = 3; lastField = new RoundedTextField("Doe"); formPanel.add(lastField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0; formPanel.add(fieldLabel("Username"), gbc);
        gbc.gridx = 1; userField = new RoundedTextField("johndoe"); formPanel.add(userField, gbc);
        gbc.gridx = 2; formPanel.add(fieldLabel("Email"), gbc);
        gbc.gridx = 3; emailField = new RoundedTextField("john@example.com"); formPanel.add(emailField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0; formPanel.add(fieldLabel("Phone"), gbc);
        gbc.gridx = 1; phoneField = new RoundedTextField("+251911000000"); formPanel.add(phoneField, gbc);
        gbc.gridx = 2; formPanel.add(fieldLabel("Gender"), gbc);
        gbc.gridx = 3; genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        genderBox.setFont(ModernTheme.FONT_BODY);
        genderBox.setBackground(ModernTheme.CARD_BG);
        formPanel.add(genderBox, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0; formPanel.add(fieldLabel("Nationality"), gbc);
        gbc.gridx = 1; natField = new RoundedTextField("Ethiopian"); formPanel.add(natField, gbc);
        gbc.gridx = 2; formPanel.add(fieldLabel("Password"), gbc);
        gbc.gridx = 3;
        JPanel passPanel = new JPanel(new BorderLayout());
        passPanel.setOpaque(false);
        passField = new RoundedPasswordField(20);
        passPanel.add(passField, BorderLayout.CENTER);
        passPanel.add(passField.getToggleButton(), BorderLayout.EAST);
        formPanel.add(passPanel, gbc);

        gbc.gridy = 5;
        gbc.gridx = 0; formPanel.add(fieldLabel("Confirm Password"), gbc);
        gbc.gridx = 1;
        JPanel confirmPanel = new JPanel(new BorderLayout());
        confirmPanel.setOpaque(false);
        confirmField = new RoundedPasswordField(20);
        confirmPanel.add(confirmField, BorderLayout.CENTER);
        confirmPanel.add(confirmField.getToggleButton(), BorderLayout.EAST);
        formPanel.add(confirmPanel, gbc);
        gbc.gridx = 2; gbc.gridwidth = 2;
        JLabel hint = new JLabel("Min 6 chars, uppercase, lowercase, digit");
        hint.setFont(ModernTheme.FONT_SMALL);
        hint.setForeground(ModernTheme.TEXT_MUTED);
        formPanel.add(hint, gbc);
        gbc.gridwidth = 1;

        gbc.gridy = 6;
        gbc.gridx = 0; gbc.gridwidth = 4;
        errorLabel = new JLabel(" ");
        errorLabel.setFont(ModernTheme.FONT_SMALL);
        errorLabel.setForeground(ModernTheme.DANGER);
        formPanel.add(errorLabel, gbc);

        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        RoundedButton regBtn = new RoundedButton("Register", ModernTheme.PRIMARY);
        regBtn.setPreferredSize(new Dimension(0, 44));
        regBtn.addActionListener(e -> attemptRegister());
        formPanel.add(regBtn, gbc);

        gbc.gridx = 2;
        RoundedButton clearBtn = new RoundedButton("Clear", ModernTheme.BORDER_LIGHT, ModernTheme.BORDER);
        clearBtn.setForeground(ModernTheme.TEXT_SECONDARY);
        clearBtn.setPreferredSize(new Dimension(0, 44));
        clearBtn.addActionListener(e -> clearFields());
        formPanel.add(clearBtn, gbc);

        gbc.gridy = 8;
        gbc.gridx = 0; gbc.gridwidth = 4;
        gbc.insets = new Insets(16, 12, 6, 12);
        JPanel links = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        links.setOpaque(false);
        JLabel login = linkLabel("Already have an account? Login");
        login.setCursor(new Cursor(Cursor.HAND_CURSOR));
        login.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) { navigator.accept("LOGIN"); }
        });
        JLabel home = linkLabel("Back to Home");
        home.setCursor(new Cursor(Cursor.HAND_CURSOR));
        home.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) { navigator.accept("HOME"); }
        });
        links.add(login);
        links.add(new JLabel("|"));
        links.add(home);
        formPanel.add(links, gbc);

        JScrollPane scroll = new JScrollPane(formPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(ModernTheme.BACKGROUND);
        add(scroll, BorderLayout.CENTER);
    }

    private void attemptRegister() {
        String first = firstField.getText().trim();
        String last = lastField.getText().trim();
        String user = userField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String pass = new String(passField.getPassword());
        String confirm = new String(confirmField.getPassword());

        errorLabel.setText(" ");

        String val = ValidationService.validateRegistration(first, last, user, email, phone, pass, confirm);
        if (val != null) {
            errorLabel.setText(val);
            return;
        }

        User newUser = new User();
        newUser.setFirstName(first);
        newUser.setLastName(last);
        newUser.setUsername(user);
        newUser.setEmail(email);
        newUser.setPhone(phone);
        newUser.setPasswordHash(pass);
        newUser.setGender((String) genderBox.getSelectedItem());
        newUser.setNationality(natField.getText().trim());

        String error = authService.register(newUser);
        if (error != null) {
            errorLabel.setText(error);
            return;
        }

        JOptionPane.showMessageDialog(this, "Account created successfully! Please login.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        navigator.accept("LOGIN");
    }

    private void clearFields() {
        firstField.setText(""); lastField.setText(""); userField.setText("");
        emailField.setText(""); phoneField.setText(""); natField.setText("");
        passField.setText(""); confirmField.setText("");
        errorLabel.setText(" ");
    }

    private JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(ModernTheme.FONT_TITLE);
        l.setForeground(ModernTheme.TEXT_PRIMARY);
        return l;
    }

    private JLabel linkLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(ModernTheme.FONT_BODY);
        l.setForeground(ModernTheme.PRIMARY);
        return l;
    }
}
