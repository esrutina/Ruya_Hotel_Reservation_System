package com.ruyahotel.ui.customer;

import com.ruyahotel.ui.components.ModernTheme;
import com.ruyahotel.ui.components.RoundedPanel;
import com.ruyahotel.ui.components.SidebarItem;
import com.ruyahotel.ui.publicpages.AboutPanel;
import com.ruyahotel.ui.publicpages.ContactPanel;
import com.ruyahotel.util.SessionManager;
import java.awt.*;
import java.util.function.Consumer;
import javax.swing.*;

/**
 * Customer dashboard wrapper with sidebar navigation.
 */
public class CustomerDashboard extends JPanel {
    private final Consumer<String> navigator;
    private final CardLayout cardLayout;
    private final JPanel contentPanel;
    private final PaymentPanel paymentPanel;
    private final FeedbackPanel feedbackPanel;
    private final SidebarItem[] items;
    private final String[] targets;

    public CustomerDashboard(Consumer<String> navigator) {
        this.navigator = navigator;
        setLayout(new BorderLayout());
        setBackground(ModernTheme.BACKGROUND);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(ModernTheme.BACKGROUND);

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(ModernTheme.SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(24, 0, 24, 0));

        JLabel logo = new JLabel("\uD83C\uDFE8 Customer");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logo.setForeground(Color.WHITE);
        logo.setBorder(BorderFactory.createEmptyBorder(0, 20, 24, 20));
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(logo);

        items = new SidebarItem[] {
                new SidebarItem("\uD83D\uDCCA", "Dashboard"),
                new SidebarItem("\uD83D\uDC64", "My Profile"),
                new SidebarItem("\uD83D\uDECF", "Book Room"),
                new SidebarItem("\uD83D\uDCCB", "Reservations"),
                new SidebarItem("\uD83D\uDCB3", "Payments"),
                new SidebarItem("\uD83D\uDCDD", "Feedback"),
                new SidebarItem("\uD83D\uDCAC", "About"),
                new SidebarItem("\uD83D\uDCDE", "Contact"),
                new SidebarItem("\uD83D\uDEAA", "Logout")
        };

        targets = new String[] {
                "CUSTOMER_DASHBOARD", "CUSTOMER_PROFILE", "CUSTOMER_BOOK",
                "CUSTOMER_RESERVATIONS", "CUSTOMER_PAYMENT", "CUSTOMER_FEEDBACK",
                "CUSTOMER_ABOUT", "CUSTOMER_CONTACT", "LOGOUT"
        };

        for (int i = 0; i < items.length; i++) {
            final int idx = i;
            items[i].onClick(() -> {
                if ("LOGOUT".equals(targets[idx])) {
                    SessionManager.getInstance().logout();
                    navigator.accept("HOME");
                } else {
                    cardLayout.show(contentPanel, targets[idx]);
                    for (SidebarItem s : items) s.setActive(false);
                    items[idx].setActive(true);
                }
            });
            items[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            sidebar.add(items[i]);
            sidebar.add(Box.createRigidArea(new Dimension(0, 4)));
        }

        contentPanel.add(dashboardHome(), "CUSTOMER_DASHBOARD");
        contentPanel.add(new ProfilePanel(navigator), "CUSTOMER_PROFILE");
        contentPanel.add(new BookRoomPanel(navigator), "CUSTOMER_BOOK");
        contentPanel.add(new ReservationPanel(navigator), "CUSTOMER_RESERVATIONS");
        paymentPanel = new PaymentPanel(navigator);
        contentPanel.add(paymentPanel, "CUSTOMER_PAYMENT");
        feedbackPanel = new FeedbackPanel(navigator);
        contentPanel.add(feedbackPanel, "CUSTOMER_FEEDBACK");
        contentPanel.add(new AboutPanel(navigator), "CUSTOMER_ABOUT");
        contentPanel.add(new ContactPanel(navigator), "CUSTOMER_CONTACT");

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    public void showPaymentWithReservation(int reservationId) {
        paymentPanel.setPreselectedReservationId(reservationId);
        paymentPanel.refreshAndSelectReservation();
        cardLayout.show(contentPanel, "CUSTOMER_PAYMENT");
        activateSidebarItem("CUSTOMER_PAYMENT");
        // Refresh feedback panel to show newly paid reservation
        if (feedbackPanel != null) {
            feedbackPanel.refreshDropdown();
        }
    }

    private void activateSidebarItem(String target) {
        for (int i = 0; i < items.length; i++) {
            items[i].setActive(target.equals(targets[i]));
        }
    }

    private JPanel dashboardHome() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ModernTheme.BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);

        JLabel welcome = new JLabel("Welcome, " + (SessionManager.getInstance().getCurrentUser() != null
                ? SessionManager.getInstance().getCurrentUser().getFirstName() : "Guest"));
        welcome.setFont(ModernTheme.FONT_HEADING);
        welcome.setForeground(ModernTheme.TEXT_PRIMARY);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3; gbc.anchor = GridBagConstraints.WEST;
        panel.add(welcome, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(statCard("\uD83D\uDECF", "Book a Room", "Explore available rooms", ModernTheme.PRIMARY, "CUSTOMER_BOOK"), gbc);
        gbc.gridx = 1;
        panel.add(statCard("\uD83D\uDCCB", "My Reservations", "View and manage bookings", ModernTheme.INFO, "CUSTOMER_RESERVATIONS"), gbc);
        gbc.gridx = 2;
        panel.add(statCard("\uD83D\uDCB3", "Make Payment", "Pay for pending bookings", ModernTheme.SUCCESS, "CUSTOMER_PAYMENT"), gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(statCard("\uD83D\uDC64", "My Profile", "Update your information", ModernTheme.WARNING, "CUSTOMER_PROFILE"), gbc);
        gbc.gridx = 1;
        panel.add(statCard("\uD83D\uDCDD", "Feedback", "Share your experience", ModernTheme.ACCENT, "CUSTOMER_FEEDBACK"), gbc);
        gbc.gridx = 2;
        // Removed the Invoices stat card

        return panel;
    }

    private RoundedPanel statCard(String icon, String title, String desc, Color accent, String target) {
        RoundedPanel card = new RoundedPanel(ModernTheme.CARD_RADIUS, ModernTheme.CARD_BG);
        card.setShowShadow(true);
        card.setPreferredSize(new Dimension(280, 140));
        card.setLayout(new GridBagLayout());
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 20, 4, 20);

        JLabel i = new JLabel(icon);
        i.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        i.setForeground(accent);
        gbc.gridy = 0;
        card.add(i, gbc);

        JLabel t = new JLabel(title);
        t.setFont(ModernTheme.FONT_SUBHEADING);
        t.setForeground(ModernTheme.TEXT_PRIMARY);
        gbc.gridy = 1;
        card.add(t, gbc);

        JLabel d = new JLabel(desc);
        d.setFont(ModernTheme.FONT_SMALL);
        d.setForeground(ModernTheme.TEXT_SECONDARY);
        gbc.gridy = 2;
        card.add(d, gbc);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                cardLayout.show(contentPanel, target);
            }
        });

        return card;
    }
}