package com.ruyahotel.ui.publicpages;

import com.ruyahotel.ui.components.ModernTheme;
import com.ruyahotel.ui.components.RoundedPanel;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class HelpPanel extends JPanel {
    public HelpPanel(Consumer<String> navigator) {
        setLayout(new BorderLayout());
        setBackground(ModernTheme.BACKGROUND);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(ModernTheme.BACKGROUND);
        content.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        JLabel title = new JLabel("Help & FAQ");
        title.setFont(ModernTheme.FONT_HEADING);
        title.setForeground(ModernTheme.TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(title);
        content.add(Box.createRigidArea(new Dimension(0, 24)));

        String[][] faqs = {
                {"How do I book a room?", "Navigate to the Rooms section, select your desired room, choose dates, and click 'Book Now'. You must be logged in to complete a booking."},
                {"How can I cancel a reservation?", "Go to your Booking History, find the reservation, and click 'Cancel'. You can only cancel before the check-in date."},
                {"What payment methods are accepted?", "We accept Telebirr mobile money payments. Enter your Telebirr transaction reference in the Payments section to confirm your reservation."},
                {"Can I modify my reservation dates?", "Currently, date changes require cancelling and rebooking. Contact reception for assistance."},
                {"Is breakfast included?", "Breakfast is included with Deluxe, Family, and Presidential Suite bookings. Standard rooms can add breakfast for 300 ETB."},
                {"What is the check-in/check-out time?", "Check-in is from 2:00 PM and check-out is by 12:00 PM. Early check-in may be available upon request."}
        };

        for (String[] faq : faqs) {
            content.add(faqCard(faq[0], faq[1]));
            content.add(Box.createRigidArea(new Dimension(0, 12)));
        }

        content.add(Box.createRigidArea(new Dimension(0, 20)));
        JLabel support = new JLabel("Need more help? Contact support@ruyahotel.com or call +251 911 000 000");
        support.setFont(ModernTheme.FONT_BODY);
        support.setForeground(ModernTheme.PRIMARY);
        support.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(support);

        add(content, BorderLayout.CENTER);
    }

    private RoundedPanel faqCard(String question, String answer) {
        RoundedPanel card = new RoundedPanel(ModernTheme.CARD_RADIUS, ModernTheme.CARD_BG);
        card.setShowShadow(true);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel q = new JLabel("Q: " + question);
        q.setFont(ModernTheme.FONT_TITLE);
        q.setForeground(ModernTheme.PRIMARY);
        card.add(q, BorderLayout.NORTH);

        JLabel a = new JLabel("<html><body style='width:700px'><p style='color:#6B7280;'>A: " + answer + "</p></body></html>");
        a.setFont(ModernTheme.FONT_BODY);
        a.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        card.add(a, BorderLayout.CENTER);

        return card;
    }
}
