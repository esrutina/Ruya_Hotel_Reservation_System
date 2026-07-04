package com.ruyahotel.ui;

import com.ruyahotel.ui.admin.*;
import com.ruyahotel.ui.components.ModernTheme;
import com.ruyahotel.ui.customer.*;
import com.ruyahotel.ui.publicpages.*;
import com.ruyahotel.util.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Main application window with CardLayout navigation.
 */
public class MainFrame extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final Map<String, JPanel> panels = new HashMap<>();
    private final Map<String, JComponent> navBars = new HashMap<>();
    private JPanel topNav;
    private JPanel sidebarNav;

    public MainFrame() {
        super("Ruya Hotel Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 800);
        setLocationRelativeTo(null);

        ModernTheme.setup();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Main container with border layout
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(ModernTheme.BACKGROUND);

        // Top navigation bar
        topNav = createTopNav();
        container.add(topNav, BorderLayout.NORTH);

        // Card layout panel
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(ModernTheme.BACKGROUND);

        // Initialize all panels
        initPanels();

        container.add(mainPanel, BorderLayout.CENTER);
        add(container);

        // Start at home
        navigate("HOME");
    }

    private void initPanels() {
        Consumer<String> navigator = this::navigate;

        // Public pages
        addPanel("HOME", new HomePanel(navigator));
        addPanel("ABOUT", new AboutPanel(navigator));
        addPanel("CONTACT", new ContactPanel(navigator));
        addPanel("HELP", new HelpPanel(navigator));
        addPanel("LOGIN", new LoginPanel(navigator));
        addPanel("REGISTER", new RegisterPanel(navigator));

        // Customer pages (will be created lazily or kept simple)
        addPanel("CUSTOMER_DASHBOARD", new CustomerDashboard(navigator));
        addPanel("CUSTOMER_PROFILE", new ProfilePanel(navigator));
        addPanel("CUSTOMER_BOOK", new BookRoomPanel(navigator));
        addPanel("CUSTOMER_RESERVATIONS", new ReservationPanel(navigator));
        addPanel("CUSTOMER_HISTORY", new BookingHistoryPanel(navigator));
        PaymentPanel paymentPanel = new PaymentPanel(navigator);
        addPanel("CUSTOMER_PAYMENT", paymentPanel);
        addPanel("CUSTOMER_FEEDBACK", new FeedbackPanel(navigator));

        // Admin pages
        addPanel("ADMIN_DASHBOARD", new AdminDashboard(navigator));
        addPanel("ADMIN_USERS", new UserManagementPanel(navigator));
        addPanel("ADMIN_ROOMS", new RoomManagementPanel(navigator));
        addPanel("ADMIN_RESERVATIONS", new ReservationManagementPanel(navigator));
        addPanel("ADMIN_PAYMENTS", new PaymentManagementPanel(navigator));
        addPanel("ADMIN_FEEDBACK", new FeedbackManagementPanel(navigator));
        addPanel("ADMIN_ROLES", new RoleManagementPanel(navigator));
        addPanel("ADMIN_REPORTS", new ReportsPanel(navigator));
        addPanel("ADMIN_SETTINGS", new SettingsPanel(navigator));
        addPanel("ADMIN_TRASH", new TrashPanel(navigator));
    }

    private void addPanel(String name, JPanel panel) {
        panels.put(name, panel);
        mainPanel.add(panel, name);
    }

    private JPanel createTopNav() {
        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(ModernTheme.CARD_BG);
        nav.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, ModernTheme.BORDER),
                BorderFactory.createEmptyBorder(12, 24, 12, 24)
        ));

        // Logo
        JLabel logo = new JLabel("\uD83C\uDFE8 Ruya Hotel");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logo.setForeground(ModernTheme.PRIMARY);
        nav.add(logo, BorderLayout.WEST);

        // Public nav links
        JPanel links = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        links.setOpaque(false);
        links.add(navLink("Home", "HOME"));
        links.add(navLink("Rooms", "HOME"));
        links.add(navLink("About", "ABOUT"));
        links.add(navLink("Contact", "CONTACT"));
        links.add(navLink("Help", "HELP"));
        nav.add(links, BorderLayout.CENTER);

        // Auth buttons
        JPanel auth = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        auth.setOpaque(false);
        auth.add(navLink("Login", "LOGIN"));
        com.ruyahotel.ui.components.RoundedButton regBtn =
                new com.ruyahotel.ui.components.RoundedButton("Register", ModernTheme.PRIMARY);
        regBtn.setRadius(8);
        regBtn.setPreferredSize(new Dimension(90, 32));
        regBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        regBtn.addActionListener(e -> navigate("REGISTER"));
        auth.add(regBtn);
        nav.add(auth, BorderLayout.EAST);

        return nav;
    }

    private JLabel navLink(String text, String target) {
        JLabel link = new JLabel(text);
        link.setFont(ModernTheme.FONT_BODY);
        link.setForeground(ModernTheme.TEXT_SECONDARY);
        link.setCursor(new Cursor(Cursor.HAND_CURSOR));
        link.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { link.setForeground(ModernTheme.PRIMARY); }
            public void mouseExited(java.awt.event.MouseEvent e) { link.setForeground(ModernTheme.TEXT_SECONDARY); }
            public void mouseClicked(java.awt.event.MouseEvent e) { navigate(target); }
        });
        return link;
    }

    public void navigate(String target) {
        if (panels.containsKey(target)) {
            cardLayout.show(mainPanel, target);
            updateNavigationFor(target);
        }
    }

    public void navigateToPaymentWithReservation(int reservationId) {
        PaymentPanel paymentPanel = (PaymentPanel) panels.get("CUSTOMER_PAYMENT");
        if (paymentPanel != null) {
            paymentPanel.setPreselectedReservationId(reservationId);
        }
        navigate("CUSTOMER_PAYMENT");
    }

    public void navigateToPaymentFromBooking(int reservationId) {
        CustomerDashboard dashboard = (CustomerDashboard) panels.get("CUSTOMER_DASHBOARD");
        if (dashboard != null) {
            dashboard.showPaymentWithReservation(reservationId);
        }
        navigate("CUSTOMER_DASHBOARD");
    }

    private void updateNavigationFor(String target) {
        // Rebuild top nav if on public page
        boolean isPublic = target.startsWith("HOME") || target.startsWith("ABOUT") || target.startsWith("CONTACT")
                || target.startsWith("HELP") || target.startsWith("LOGIN") || target.startsWith("REGISTER");

        // For dashboard pages we could hide top nav or change it, but let's keep it simple
        if (target.startsWith("ADMIN_") || target.startsWith("CUSTOMER_")) {
            // Could show different nav, but for simplicity we keep top nav and let dashboards have sidebars
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
