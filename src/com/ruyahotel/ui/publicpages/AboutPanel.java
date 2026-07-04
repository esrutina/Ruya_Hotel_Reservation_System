package com.ruyahotel.ui.publicpages;

import com.ruyahotel.dao.SettingsDAO;
import com.ruyahotel.ui.components.ModernTheme;
import com.ruyahotel.ui.components.RoundedPanel;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class AboutPanel extends JPanel {
    private final SettingsDAO settingsDAO = new SettingsDAO();

    public AboutPanel(Consumer<String> navigator) {
        setLayout(new BorderLayout());
        setBackground(ModernTheme.BACKGROUND);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(ModernTheme.BACKGROUND);
        content.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        JLabel title = new JLabel(settingsDAO.getValue("hotel_name") != null ? settingsDAO.getValue("hotel_name") : "Ruya Hotel");
        title.setFont(ModernTheme.FONT_HEADING);
        title.setForeground(ModernTheme.TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(title);
        content.add(Box.createRigidArea(new Dimension(0, 20)));

        String story = settingsDAO.getValue("about_story");
        if (story == null || story.isEmpty()) {
            story = "Ruya Hotel was founded with a vision to provide world-class hospitality in the heart of Ethiopia. "
                    + "Our name 'Ruya' means 'vision' — and we strive every day to create memorable experiences for our guests. "
                    + "From our elegantly designed rooms to our award-winning restaurant, every detail is crafted with care.";
        }
        JLabel storyLabel = new JLabel("<html><body style='width:700px'><p style='font-size:14px; color:#374151;'>" + story + "</p></body></html>");
        storyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(storyLabel);
        content.add(Box.createRigidArea(new Dimension(0, 30)));

        JLabel servicesTitle = new JLabel("Our Services");
        servicesTitle.setFont(ModernTheme.FONT_SUBHEADING);
        servicesTitle.setForeground(ModernTheme.TEXT_PRIMARY);
        servicesTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(servicesTitle);
        content.add(Box.createRigidArea(new Dimension(0, 16)));

        JPanel servicesGrid = new JPanel(new GridLayout(2, 3, 16, 16));
        servicesGrid.setOpaque(false);
        servicesGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        servicesGrid.setMaximumSize(new Dimension(800, 240));

        String[][] items = {
                {"\uD83D\uDECF","Luxury Rooms"},
                {"\uD83C\uDF7D","Fine Dining"},
                {"\uD83C\uDFC6","Fitness Center"},
                {"\uD83D\uDC86","Spa & Wellness"},
                {"\uD83D\uDD0A","Conference Hall"},
                {"\uD83D\uDE97","Airport Shuttle"}
        };
        for (String[] item : items) {
            servicesGrid.add(serviceCard(item[0], item[1]));
        }

        content.add(servicesGrid);
        content.add(Box.createRigidArea(new Dimension(0, 30)));

        String mission = settingsDAO.getValue("mission");
        String vision = settingsDAO.getValue("vision");
        if (mission == null) mission = "To deliver exceptional hospitality that exceeds expectations.";
        if (vision == null) vision = "To become the most preferred luxury hotel in East Africa.";
        JLabel missionLabel = new JLabel("<html><body style='width:700px'><b>Mission:</b> " + mission + "<br><br>"
                + "<b>Vision:</b> " + vision + "</body></html>");
        missionLabel.setFont(ModernTheme.FONT_BODY);
        missionLabel.setForeground(ModernTheme.TEXT_SECONDARY);
        missionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(missionLabel);

        add(content, BorderLayout.CENTER);
    }

    private RoundedPanel serviceCard(String icon, String name) {
        RoundedPanel card = new RoundedPanel(12, ModernTheme.CARD_BG);
        card.setShowShadow(true);
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel i = new JLabel(icon);
        i.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        gbc.gridy = 0;
        card.add(i, gbc);

        JLabel n = new JLabel(name);
        n.setFont(ModernTheme.FONT_TITLE);
        n.setForeground(ModernTheme.TEXT_PRIMARY);
        gbc.gridy = 1;
        card.add(n, gbc);

        return card;
    }
}
