package com.ruyahotel.ui.publicpages;

import com.ruyahotel.service.AuthService;
import com.ruyahotel.ui.components.*;
import com.ruyahotel.util.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class LoginPanel extends JPanel {
    private final Consumer<String> navigator;
    private final AuthService authService = new AuthService();
    private RoundedTextField userField;
    private RoundedPasswordField passField;
    private JLabel errorLabel;

    public LoginPanel(Consumer<String> navigator) {
        this.navigator = navigator;
        setLayout(new GridBagLayout());
        setBackground(ModernTheme.BACKGROUND);

        buildForm();
    }

    private void buildForm() {
        RoundedPanel card = new RoundedPanel(ModernTheme.CARD_RADIUS, ModernTheme.CARD_BG);
        card.setShowShadow(true);
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(420, 480));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 24, 8, 24);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JLabel title = new JLabel("Welcome Back");
        title.setFont(ModernTheme.FONT_HEADING);
        title.setForeground(ModernTheme.TEXT_PRIMARY);
        gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        card.add(title, gbc);

        JLabel sub = new JLabel("Sign in to your account");
        sub.setFont(ModernTheme.FONT_BODY);
        sub.setForeground(ModernTheme.TEXT_SECONDARY);
        gbc.gridy = 1;
        card.add(sub, gbc);

        gbc.insets = new Insets(16, 24, 4, 24);
        gbc.gridy = 2;
        card.add(fieldLabel("Username or Email"), gbc);

        gbc.insets = new Insets(0, 24, 8, 24);
        gbc.gridy = 3;
        userField = new RoundedTextField("Enter username or email");
        userField.setPreferredSize(new Dimension(0, 44));
        card.add(userField, gbc);

        gbc.insets = new Insets(8, 24, 4, 24);
        gbc.gridy = 4;
        card.add(fieldLabel("Password"), gbc);

        gbc.insets = new Insets(0, 24, 8, 24);
        gbc.gridy = 5;
        JPanel passPanel = new JPanel(new BorderLayout());
        passPanel.setOpaque(false);
        passField = new RoundedPasswordField(20);
        passField.setPreferredSize(new Dimension(0, 44));
        passPanel.add(passField, BorderLayout.CENTER);
        passPanel.add(passField.getToggleButton(), BorderLayout.EAST);
        card.add(passPanel, gbc);

        gbc.insets = new Insets(4, 24, 4, 24);
        gbc.gridy = 6;
        errorLabel = new JLabel(" ");
        errorLabel.setFont(ModernTheme.FONT_SMALL);
        errorLabel.setForeground(ModernTheme.DANGER);
        card.add(errorLabel, gbc);

        gbc.insets = new Insets(12, 24, 8, 24);
        gbc.gridy = 7;
        RoundedButton loginBtn = new RoundedButton("Sign In", ModernTheme.PRIMARY);
        loginBtn.setRadius(10);
        loginBtn.setPreferredSize(new Dimension(0, 48));
        loginBtn.addActionListener(e -> attemptLogin());
        card.add(loginBtn, gbc);

        gbc.insets = new Insets(4, 24, 4, 24);
        gbc.gridy = 8;
        RoundedButton clearBtn = new RoundedButton("Clear", ModernTheme.BORDER_LIGHT, ModernTheme.BORDER);
        clearBtn.setForeground(ModernTheme.TEXT_SECONDARY);
        clearBtn.setPreferredSize(new Dimension(0, 40));
        clearBtn.addActionListener(e -> clearFields());
        card.add(clearBtn, gbc);

        gbc.insets = new Insets(12, 24, 8, 24);
        gbc.gridy = 9;
        JPanel links = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        links.setOpaque(false);

        JLabel forgot = linkLabel("Forgot Password?");
        forgot.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgot.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                navigator.accept("CONTACT");
            }
        });

        JLabel reg = linkLabel("Register new account");
        reg.setCursor(new Cursor(Cursor.HAND_CURSOR));
        reg.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                navigator.accept("REGISTER");
            }
        });

        links.add(forgot);
        links.add(new JLabel("|"));
        links.add(reg);
        card.add(links, gbc);

        gbc.gridy = 10;
        JLabel home = linkLabel("Back to Home");
        home.setCursor(new Cursor(Cursor.HAND_CURSOR));
        home.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                navigator.accept("HOME");
            }
        });
        card.add(home, gbc);

        add(card);
    }

    private void attemptLogin() {
        String user = userField.getText().trim();
        String pass = new String(passField.getPassword());
        errorLabel.setText(" ");

        String error = authService.login(user, pass);
        if (error != null) {
            errorLabel.setText(error);
            return;
        }

        if (SessionManager.getInstance().isAdmin()) {
            navigator.accept("ADMIN_DASHBOARD");
        } else {
            navigator.accept("CUSTOMER_DASHBOARD");
        }
    }

    private void clearFields() {
        userField.setText("");
        passField.setText("");
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
