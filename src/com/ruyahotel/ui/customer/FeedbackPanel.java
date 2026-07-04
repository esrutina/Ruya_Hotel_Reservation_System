package com.ruyahotel.ui.customer;

import com.ruyahotel.model.Feedback;
import com.ruyahotel.model.Reservation;
import com.ruyahotel.service.FeedbackService;
import com.ruyahotel.service.ReservationService;
import com.ruyahotel.ui.components.*;
import com.ruyahotel.util.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class FeedbackPanel extends JPanel {
    private final Consumer<String> navigator;
    private final FeedbackService feedbackService = new FeedbackService();
    private final ReservationService reservationService = new ReservationService();
    private JComboBox<String> reservationBox;
    private JComboBox<Integer> ratingBox;
    private JTextArea commentArea;
    private JLabel msgLabel;

    public FeedbackPanel(Consumer<String> navigator) {
        this.navigator = navigator;
        setLayout(new BorderLayout());
        setBackground(ModernTheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buildForm(), buildList());
        split.setDividerLocation(420);
        split.setBorder(null);
        split.setBackground(ModernTheme.BACKGROUND);
        add(split, BorderLayout.CENTER);
        
        // Initial load
        loadReservationsForDropdown();
    }
    
    /**
     * Refresh the reservation dropdown to reflect latest payment statuses
     */
    public void refreshDropdown() {
        loadReservationsForDropdown();
    }

    private JPanel buildForm() {
        RoundedPanel form = new RoundedPanel(ModernTheme.CARD_RADIUS, ModernTheme.CARD_BG);
        form.setShowShadow(true);
        form.setLayout(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel title = new JLabel("Write Feedback");
        title.setFont(ModernTheme.FONT_SUBHEADING);
        title.setForeground(ModernTheme.TEXT_PRIMARY);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        form.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0; form.add(fieldLabel("Reservation"), gbc);
        gbc.gridx = 1; reservationBox = new JComboBox<>(); reservationBox.setFont(ModernTheme.FONT_BODY); form.add(reservationBox, gbc);

        gbc.gridy = 2; gbc.gridx = 0; form.add(fieldLabel("Rating"), gbc);
        gbc.gridx = 1; ratingBox = new JComboBox<>(new Integer[]{1,2,3,4,5}); ratingBox.setFont(ModernTheme.FONT_BODY); form.add(ratingBox, gbc);

        gbc.gridy = 3; gbc.gridx = 0; form.add(fieldLabel("Comment"), gbc);
        gbc.gridx = 1; commentArea = new JTextArea(4, 20);
        commentArea.setFont(ModernTheme.FONT_BODY);
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        commentArea.setBorder(ModernTheme.inputBorder());
        form.add(new JScrollPane(commentArea), gbc);

        gbc.gridy = 4; gbc.gridx = 1;
        msgLabel = new JLabel(" ");
        msgLabel.setFont(ModernTheme.FONT_SMALL);
        msgLabel.setForeground(ModernTheme.SUCCESS);
        form.add(msgLabel, gbc);

        gbc.gridy = 5; gbc.gridx = 1;
        RoundedButton submitBtn = new RoundedButton("Submit Feedback", ModernTheme.PRIMARY);
        submitBtn.setPreferredSize(new Dimension(160, 44));
        submitBtn.addActionListener(e -> submitFeedback());
        form.add(submitBtn, gbc);

        loadReservationsForDropdown();
        return form;
    }

    private JPanel buildList() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ModernTheme.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));

        JLabel title = new JLabel("My Past Feedback");
        title.setFont(ModernTheme.FONT_SUBHEADING);
        title.setForeground(ModernTheme.TEXT_PRIMARY);
        panel.add(title, BorderLayout.NORTH);

        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setOpaque(false);

        List<Feedback> feedbacks = feedbackService.getByUser(SessionManager.getInstance().getUserId());
        for (Feedback f : feedbacks) {
            RoundedPanel card = new RoundedPanel(12, ModernTheme.CARD_BG);
            card.setShowShadow(true);
            card.setLayout(new BorderLayout());
            card.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
            card.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel stars = new JLabel(f.getStarDisplay());
            stars.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            stars.setForeground(ModernTheme.ACCENT);

            JLabel room = new JLabel(f.getRoomType() + " - " + f.getCreatedAt().toLocalDate());
            room.setFont(ModernTheme.FONT_SMALL);
            room.setForeground(ModernTheme.TEXT_SECONDARY);

            JLabel comment = new JLabel("<html><body style='width:300px'>" + f.getComment() + "</body></html>");
            comment.setFont(ModernTheme.FONT_BODY);
            comment.setForeground(ModernTheme.TEXT_PRIMARY);

            JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
            top.setOpaque(false);
            top.add(stars);
            top.add(room);

            card.add(top, BorderLayout.NORTH);
            card.add(comment, BorderLayout.CENTER);
            list.add(card);
            list.add(Box.createRigidArea(new Dimension(0, 12)));
        }

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(ModernTheme.BACKGROUND);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private void loadReservationsForDropdown() {
        reservationBox.removeAllItems();
        List<Reservation> list = reservationService.getUserReservations(SessionManager.getInstance().getUserId());
        for (Reservation r : list) {
            // Show reservations that are confirmed, checked-in, checked-out OR have Paid payment status
            if ("Confirmed".equals(r.getStatus()) || "Checked-Out".equals(r.getStatus()) || "Checked-In".equals(r.getStatus())
                || "Paid".equalsIgnoreCase(r.getPaymentStatus())) {
                reservationBox.addItem("#" + r.getReservationId() + " - " + r.getRoomType());
            }
        }
        if (reservationBox.getItemCount() == 0) {
            reservationBox.addItem("No eligible reservations found");
        }
    }

    private void submitFeedback() {
        String selected = (String) reservationBox.getSelectedItem();
        if (selected == null || "No eligible reservations found".equals(selected)) {
            msgLabel.setText("No eligible reservation found.");
            msgLabel.setForeground(ModernTheme.DANGER);
            return;
        }
        int resId = Integer.parseInt(selected.split(" - ")[0].replace("#", ""));

        List<Reservation> list = reservationService.getUserReservations(SessionManager.getInstance().getUserId());
        Reservation res = list.stream().filter(r -> r.getReservationId() == resId).findFirst().orElse(null);
        if (res == null) return;

        Feedback f = new Feedback();
        f.setReservationId(resId);
        f.setUserId(SessionManager.getInstance().getUserId());
        f.setRoomId(res.getRoomId());
        f.setRating((Integer) ratingBox.getSelectedItem());
        f.setComment(commentArea.getText().trim());

        String error = feedbackService.submitFeedback(f);
        if (error != null) {
            msgLabel.setText(error);
            msgLabel.setForeground(ModernTheme.DANGER);
        } else {
            msgLabel.setText("Thank you for your feedback!");
            msgLabel.setForeground(ModernTheme.SUCCESS);
            commentArea.setText("");
            revalidate(); repaint();
        }
    }

    private JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(ModernTheme.FONT_TITLE);
        l.setForeground(ModernTheme.TEXT_PRIMARY);
        return l;
    }
}
