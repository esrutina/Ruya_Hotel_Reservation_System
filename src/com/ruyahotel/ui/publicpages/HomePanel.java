package com.ruyahotel.ui.publicpages;

import com.ruyahotel.model.Room;
import com.ruyahotel.service.RoomService;
import com.ruyahotel.ui.components.*;
import com.ruyahotel.util.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

/**
 * Public Home Page with hero banner, room preview, testimonials, and contact info.
 */
public class HomePanel extends JPanel {
    private final Consumer<String> navigator;
    private final RoomService roomService = new RoomService();

    public HomePanel(Consumer<String> navigator) {
        this.navigator = navigator;
        setLayout(new BorderLayout());
        setBackground(ModernTheme.BACKGROUND);

        JScrollPane scroll = new JScrollPane(buildContent());
        scroll.setBorder(null);
        scroll.getViewport().setBackground(ModernTheme.BACKGROUND);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel buildContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(ModernTheme.BACKGROUND);

        content.add(heroSection());
        content.add(Box.createRigidArea(new Dimension(0, 40)));
        content.add(infoSection());
        content.add(Box.createRigidArea(new Dimension(0, 40)));
        content.add(roomsSection());
        content.add(Box.createRigidArea(new Dimension(0, 40)));
        content.add(gallerySection());
        content.add(Box.createRigidArea(new Dimension(0, 40)));
        content.add(testimonialsSection());
        content.add(Box.createRigidArea(new Dimension(0, 40)));
        content.add(contactSection());
        content.add(Box.createRigidArea(new Dimension(0, 40)));
        content.add(footerSection());

        return content;
    }

    private JPanel heroSection() {
        RoundedPanel hero = new RoundedPanel(0, ModernTheme.PRIMARY);
        hero.setCustomPaint(ModernTheme.primaryGradient(400));
        hero.setShowShadow(false);
        hero.setPreferredSize(new Dimension(0, 400));
        hero.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        JLabel welcome = new JLabel("Welcome to Ruya Hotel");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 42));
        welcome.setForeground(Color.WHITE);
        gbc.gridy = 0;
        hero.add(welcome, gbc);

        JLabel tagline = new JLabel("Luxury and Comfort for Your Stay");
        tagline.setFont(ModernTheme.FONT_SUBHEADING);
        tagline.setForeground(new Color(255, 255, 255, 220));
        gbc.gridy = 1;
        hero.add(tagline, gbc);

        RoundedButton bookBtn = new RoundedButton("Book Now", ModernTheme.ACCENT, ModernTheme.ACCENT_HOVER);
        bookBtn.setRadius(30);
        bookBtn.setPreferredSize(new Dimension(160, 48));
        bookBtn.addActionListener(e -> navigator.accept("LOGIN"));
        gbc.gridy = 2;
        gbc.insets = new Insets(24, 0, 10, 0);
        hero.add(bookBtn, gbc);

        return hero;
    }

    private JPanel infoSection() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 24, 0));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        panel.add(infoCard("\uD83D\uDCF6", "Free WiFi", "High-speed internet"));
        panel.add(infoCard("\uD83D\uDE97", "Free Parking", "Secure parking lot"));
        panel.add(infoCard("\uD83C\uDF21", "Air Conditioned", "Climate control"));
        panel.add(infoCard("\uD83C\uDF7D", "Restaurant", "Fine dining"));

        return panel;
    }

    private RoundedPanel infoCard(String icon, String title, String desc) {
        RoundedPanel card = new RoundedPanel(ModernTheme.CARD_RADIUS, ModernTheme.CARD_BG);
        card.setShowShadow(true);
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(220, 100));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(4, 0, 4, 0);

        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        gbc.gridy = 0;
        card.add(iconLbl, gbc);

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(ModernTheme.FONT_TITLE);
        titleLbl.setForeground(ModernTheme.TEXT_PRIMARY);
        gbc.gridy = 1;
        card.add(titleLbl, gbc);

        JLabel descLbl = new JLabel(desc);
        descLbl.setFont(ModernTheme.FONT_SMALL);
        descLbl.setForeground(ModernTheme.TEXT_SECONDARY);
        gbc.gridy = 2;
        card.add(descLbl, gbc);

        return card;
    }

    private JPanel roomsSection() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        JLabel title = new JLabel("Featured Rooms");
        title.setFont(ModernTheme.FONT_HEADING);
        title.setForeground(ModernTheme.TEXT_PRIMARY);
        wrapper.add(title, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(1, 0, 24, 24));
        grid.setOpaque(false);
        grid.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        for (Room room : roomService.getAvailableRooms()) {
            if (grid.getComponentCount() >= 4) break;
            RoomCard card = new RoomCard(room);
            card.getActionButton().addActionListener(e -> navigator.accept("LOGIN"));
            grid.add(card);
        }

        wrapper.add(grid, BorderLayout.CENTER);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 450));
        return wrapper;
    }

    private JPanel gallerySection() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        JLabel title = new JLabel("Gallery");
        title.setFont(ModernTheme.FONT_HEADING);
        title.setForeground(ModernTheme.TEXT_PRIMARY);
        wrapper.add(title, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 3, 16, 16));
        grid.setOpaque(false);
        grid.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        String[] imageFiles = {"family.jpg", "suite.jpg", "pool.jpg", "dining.jpg", "gym.jpg", "spa.jpg"};
        String[] labels = {"Lobby", "Suite", "Pool", "Dining", "Gym", "Spa"};

        for (int i = 0; i < 6; i++) {
            RoundedPanel img = new RoundedPanel(12, ModernTheme.CARD_BG);
            img.setPreferredSize(new Dimension(0, 140));
            img.setLayout(new BorderLayout());

            JLabel imgLabel = new JLabel(labels[i], SwingConstants.CENTER);
            imgLabel.setFont(ModernTheme.FONT_TITLE);
            imgLabel.setForeground(ModernTheme.TEXT_SECONDARY);

            ImageIcon icon = ImageLoader.loadIcon(imageFiles[i], 200, 120);
            if (icon != null) {
                imgLabel.setText("");
                imgLabel.setIcon(icon);
            }

            img.add(imgLabel, BorderLayout.CENTER);
            grid.add(img);
        }

        wrapper.add(grid, BorderLayout.CENTER);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
        return wrapper;
    }

    private JPanel testimonialsSection() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        JLabel title = new JLabel("What Our Guests Say");
        title.setFont(ModernTheme.FONT_HEADING);
        title.setForeground(ModernTheme.TEXT_PRIMARY);
        wrapper.add(title, BorderLayout.NORTH);

        JPanel flow = new JPanel(new FlowLayout(FlowLayout.CENTER, 24, 0));
        flow.setOpaque(false);
        flow.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        flow.add(testimonialCard("Abebe K.", "Amazing stay! The staff were incredibly helpful and the room was spotless.", 5));
        flow.add(testimonialCard("Saron T.", "Best hotel in Addis. The presidential suite is worth every penny.", 5));
        flow.add(testimonialCard("Dawit M.", "Great location, modern facilities, and excellent breakfast.", 4));

        wrapper.add(flow, BorderLayout.CENTER);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));
        return wrapper;
    }

    private RoundedPanel testimonialCard(String name, String text, int stars) {
        RoundedPanel card = new RoundedPanel(ModernTheme.CARD_RADIUS, ModernTheme.CARD_BG);
        card.setShowShadow(true);
        card.setPreferredSize(new Dimension(320, 180));
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel nameLbl = new JLabel(name);
        nameLbl.setFont(ModernTheme.FONT_TITLE);
        nameLbl.setForeground(ModernTheme.TEXT_PRIMARY);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stars; i++) sb.append("\u2605");
        for (int i = stars; i < 5; i++) sb.append("\u2606");
        JLabel starsLbl = new JLabel(sb.toString());
        starsLbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        starsLbl.setForeground(ModernTheme.ACCENT);

        JLabel textLbl = new JLabel("<html><body style='width:260px'>" + text + "</body></html>");
        textLbl.setFont(ModernTheme.FONT_BODY);
        textLbl.setForeground(ModernTheme.TEXT_SECONDARY);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        top.setOpaque(false);
        top.add(nameLbl);
        top.add(starsLbl);

        card.add(top, BorderLayout.NORTH);
        card.add(textLbl, BorderLayout.CENTER);
        return card;
    }

    private JPanel contactSection() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        JLabel title = new JLabel("Contact Us");
        title.setFont(ModernTheme.FONT_HEADING);
        title.setForeground(ModernTheme.TEXT_PRIMARY);
        wrapper.add(title, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(1, 3, 24, 0));
        grid.setOpaque(false);
        grid.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        grid.add(contactCard("\uD83D\uDCDE", "Phone", "+251 911 000 000"));
        grid.add(contactCard("\uD83D\uDCE7", "Email", "info@ruyahotel.com"));
        grid.add(contactCard("\uD83D\uDCCD", "Address", "Addis Ababa, Ethiopia"));

        wrapper.add(grid, BorderLayout.CENTER);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        return wrapper;
    }

    private RoundedPanel contactCard(String icon, String title, String value) {
        RoundedPanel card = new RoundedPanel(ModernTheme.CARD_RADIUS, ModernTheme.CARD_BG);
        card.setShowShadow(true);
        card.setPreferredSize(new Dimension(0, 120));
        card.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(4, 0, 4, 0);

        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        gbc.gridy = 0;
        card.add(iconLbl, gbc);

        JLabel t = new JLabel(title);
        t.setFont(ModernTheme.FONT_TITLE);
        t.setForeground(ModernTheme.TEXT_PRIMARY);
        gbc.gridy = 1;
        card.add(t, gbc);

        JLabel v = new JLabel(value);
        v.setFont(ModernTheme.FONT_BODY);
        v.setForeground(ModernTheme.TEXT_SECONDARY);
        gbc.gridy = 2;
        card.add(v, gbc);

        return card;
    }

    private JPanel footerSection() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(ModernTheme.SIDEBAR_BG);
        footer.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        footer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JLabel copy = new JLabel("\u00A9 2025 Ruya Hotel. All rights reserved.");
        copy.setFont(ModernTheme.FONT_SMALL);
        copy.setForeground(ModernTheme.TEXT_MUTED);
        footer.add(copy, BorderLayout.WEST);

        JLabel social = new JLabel("\uD83D\uDCE2  \uD83D\uDCF1  \uD83D\uDCAC");
        social.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        social.setForeground(ModernTheme.TEXT_MUTED);
        footer.add(social, BorderLayout.EAST);

        return footer;
    }
}
