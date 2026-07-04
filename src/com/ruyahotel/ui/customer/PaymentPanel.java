package com.ruyahotel.ui.customer;

import com.ruyahotel.model.Payment;
import com.ruyahotel.model.Reservation;
import com.ruyahotel.service.PaymentService;
import com.ruyahotel.service.ReservationService;
import com.ruyahotel.service.TelebirrService;
import com.ruyahotel.ui.components.*;
import com.ruyahotel.util.ReceiptGenerator;
import com.ruyahotel.util.SessionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class PaymentPanel extends JPanel {
    private final Consumer<String> navigator;
    private final PaymentService paymentService = new PaymentService();
    private final ReservationService reservationService = new ReservationService();
    private ModernTable table;
    private DefaultTableModel model;
    private RoundedTextField txRefField;
    private int preselectedReservationId = -1;

    /**
     * Cache: paymentId -> TelebirrTransactionDetails
     * Populated after a successful Verify & Pay so Download Receipt
     * never needs to hit the API a second time.
     */
    private final Map<Integer, TelebirrService.TelebirrTransactionDetails> txCache = new HashMap<>();

    public PaymentPanel(Consumer<String> navigator) {
        this.navigator = navigator;
        setLayout(new BorderLayout());
        setBackground(ModernTheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("Payments");
        title.setFont(ModernTheme.FONT_HEADING);
        title.setForeground(ModernTheme.TEXT_PRIMARY);
        add(title, BorderLayout.NORTH);

        String[] cols = {"Payment ID", "Reservation", "Amount (ETB)", "Method", "Status", "Date"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new ModernTable(model);

        // Double-click a row to see transaction details
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showTransactionDetails();
                }
            }
        });

        add(table.wrapInScrollPane(), BorderLayout.CENTER);

        // ── Bottom card ───────────────────────────────────────────────────────
        RoundedPanel bottomCard = new RoundedPanel(12, ModernTheme.CARD_BG);
        bottomCard.setShowShadow(true);
        bottomCard.setLayout(new BorderLayout());

        // Left: Telebirr reference input + Verify & Pay
        JPanel leftSide = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 4));
        leftSide.setOpaque(false);

        JLabel telebirrIcon = new JLabel("⚡");
        telebirrIcon.setFont(new Font("Segoe UI", Font.BOLD, 18));
        telebirrIcon.setForeground(ModernTheme.ACCENT);
        leftSide.add(telebirrIcon);

        JLabel refLabel = new JLabel("Telebirr Ref:");
        refLabel.setFont(ModernTheme.FONT_TITLE);
        refLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        leftSide.add(refLabel);

        txRefField = new RoundedTextField(15);
        txRefField.setPlaceholder("e.g. CE626EJRNS");
        txRefField.setPreferredSize(new Dimension(160, 36));
        leftSide.add(txRefField);

        RoundedButton verifyBtn = new RoundedButton("Verify & Pay", ModernTheme.SUCCESS);
        verifyBtn.setPreferredSize(new Dimension(120, 36));
        verifyBtn.setFont(ModernTheme.FONT_BUTTON);
        verifyBtn.addActionListener(e -> verifyAndPay());
        leftSide.add(verifyBtn);

        bottomCard.add(leftSide, BorderLayout.WEST);

        // Right: View Details + Download Receipt + Refresh
        JPanel rightSide = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 4));
        rightSide.setOpaque(false);

        RoundedButton detailBtn = new RoundedButton("📋 View Details", ModernTheme.INFO);
        detailBtn.setPreferredSize(new Dimension(140, 36));
        detailBtn.setFont(ModernTheme.FONT_BUTTON);
        detailBtn.addActionListener(e -> showTransactionDetails());
        rightSide.add(detailBtn);

        RoundedButton receiptBtn = new RoundedButton("⬇ Download Receipt", ModernTheme.PRIMARY);
        receiptBtn.setPreferredSize(new Dimension(170, 36));
        receiptBtn.setFont(ModernTheme.FONT_BUTTON);
        receiptBtn.addActionListener(e -> downloadReceipt());
        rightSide.add(receiptBtn);

        RoundedButton refreshBtn = new RoundedButton("Refresh", new Color(0x607D8B));
        refreshBtn.setPreferredSize(new Dimension(100, 36));
        refreshBtn.setFont(ModernTheme.FONT_BUTTON);
        refreshBtn.addActionListener(e -> loadPayments());
        rightSide.add(refreshBtn);

        bottomCard.add(rightSide, BorderLayout.EAST);

        JPanel bottomContainer = new JPanel(new BorderLayout());
        bottomContainer.setOpaque(false);
        bottomContainer.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));
        bottomContainer.add(bottomCard, BorderLayout.CENTER);

        add(bottomContainer, BorderLayout.SOUTH);
        loadPayments();
    }

    public void setPreselectedReservationId(int reservationId) {
        this.preselectedReservationId = reservationId;
    }

    public void refreshAndSelectReservation() {
        loadPayments();
        selectReservationRow();
    }

    // ── Load payments table ───────────────────────────────────────────────────
    private void loadPayments() {
        model.setRowCount(0);
        List<Payment> list = paymentService.getUserPayments(SessionManager.getInstance().getUserId());
        for (Payment p : list) {
            model.addRow(new Object[]{
                    p.getPaymentId(), p.getReservationId(),
                    String.format("%,.2f", p.getAmount()),
                    p.getPaymentMethod(), p.getStatus(),
                    p.getCreatedAt() != null ? p.getCreatedAt().toLocalDate().toString() : ""
            });
        }
        // Show pending (unpaid) reservations that have no payment record yet
        List<Reservation> reservations = reservationService.getUserReservations(SessionManager.getInstance().getUserId());
        for (Reservation r : reservations) {
            if ("Pending".equals(r.getPaymentStatus())) {
                // Check if already shown in payments list
                boolean alreadyListed = false;
                for (int i = 0; i < model.getRowCount(); i++) {
                    Object resId = model.getValueAt(i, 1);
                    if (resId != null && String.valueOf(r.getReservationId()).equals(resId.toString())) {
                        alreadyListed = true;
                        break;
                    }
                }
                if (!alreadyListed) {
                    model.addRow(new Object[]{"-", r.getReservationId(),
                            String.format("%,.2f", r.getTotalPrice()),
                            "Pending", "UNPAID",
                            r.getCreatedAt() != null ? r.getCreatedAt().toLocalDate().toString() : ""});
                }
            }
        }
        if (preselectedReservationId > 0) {
            selectReservationRow();
        }
    }

    private void selectReservationRow() {
        for (int i = 0; i < model.getRowCount(); i++) {
            Object resId = model.getValueAt(i, 1);
            if (resId != null && String.valueOf(preselectedReservationId).equals(resId.toString())) {
                table.setRowSelectionInterval(i, i);
                table.scrollRectToVisible(table.getCellRect(i, 0, true));
                break;
            }
        }
    }

    // ── Verify & Pay ──────────────────────────────────────────────────────────
    private void verifyAndPay() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a reservation from the table first.");
            return;
        }

        Object resObj = model.getValueAt(row, 1);
        int reservationId;
        try {
            reservationId = Integer.parseInt(resObj.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid reservation.");
            return;
        }

        String txRef = txRefField.getText().trim();
        if (txRef.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter the Telebirr Transaction Reference first.",
                    "Missing Reference", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Object statusObj = model.getValueAt(row, 4);
        if (statusObj != null && "Paid".equalsIgnoreCase(statusObj.toString())) {
            JOptionPane.showMessageDialog(this,
                    "This reservation has already been paid.",
                    "Already Paid", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Reservation r = reservationService.getById(reservationId);
        if (r == null) {
            JOptionPane.showMessageDialog(this, "Reservation not found.");
            return;
        }

        // ── Telebirr Verification ─────────────────────────────────────────────
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        TelebirrService telebirrService = new TelebirrService();
        TelebirrService.VerificationResult result = telebirrService.verifyTelebirrPayment(txRef);
        setCursor(Cursor.getDefaultCursor());

        if (!result.success) {
            JOptionPane.showMessageDialog(
                    this,
                    "❌ Payment Verification Failed\n\n" + result.errorMessage
                    + "\n\nPlease make sure:\n"
                    + "  • The transaction reference is correct\n"
                    + "  • The Telebirr transfer has been completed\n"
                    + "  • You are using your own transaction reference",
                    "Verification Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        TelebirrService.TelebirrTransactionDetails tx = result.data;

        if (!"Completed".equalsIgnoreCase(tx.transactionStatus)) {
            JOptionPane.showMessageDialog(
                    this,
                    "❌ Transaction status is: " + tx.transactionStatus + "\n"
                    + "Payment must be 'Completed' to confirm.\n\n"
                    + "Please complete the Telebirr transfer and try again.",
                    "Transaction Not Completed",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // ── Amount confirmation ───────────────────────────────────────────────
        double reservationAmount = r.getTotalPrice();
        if ("Simulated".equals(tx.settledAmount)) {
            tx.settledAmount = String.format("%,.2f Birr", reservationAmount);
            tx.totalPaidAmount = String.format("%,.2f Birr", reservationAmount);
        }

        String amountInfo = String.format(
                "Reservation Amount : ETB %,.2f\n" +
                "Telebirr Settled   : %s\n" +
                "Total Paid (incl. fee): %s",
                reservationAmount, tx.settledAmount, tx.totalPaidAmount);

        // ── Save payment in DB ────────────────────────────────────────────────
        Payment payment = paymentService.processPayment(
                reservationId,
                SessionManager.getInstance().getUserId(),
                "Telebirr",
                txRef
        );

        if (payment == null) {
            JOptionPane.showMessageDialog(this, "Failed to save payment record.\nThe reservation may already be paid or does not belong to your account.");
            return;
        }

        String confirmError = paymentService.confirmPayment(payment.getPaymentId());

        // ── Cache the Telebirr details for this payment ───────────────────────
        txCache.put(payment.getPaymentId(), tx);

        if (confirmError != null) {
            JOptionPane.showMessageDialog(
                    this,
                    "⚠ Payment registered, but auto-confirmation error:\n" + confirmError,
                    "Auto-Confirmation Alert",
                    JOptionPane.WARNING_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "✅ PAYMENT VERIFIED & APPROVED!\n\n"
                    + "── Transaction Details ──────────────────\n"
                    + "Receipt No        : " + tx.receiptNo + "\n"
                    + "Payer             : " + tx.payerName + "\n"
                    + "Payer Telebirr    : " + tx.payerTelebirrNo + "\n"
                    + "Credited To       : " + tx.creditedPartyName + "\n"
                    + "── Amount Breakdown ─────────────────────\n"
                    + "Settled Amount    : " + tx.settledAmount + "\n"
                    + "Service Fee (VAT) : " + tx.serviceFeeVAT + "\n"
                    + "Total Paid        : " + tx.totalPaidAmount + "\n"
                    + "Payment Date      : " + tx.paymentDate + "\n"
                    + "─────────────────────────────────────────\n"
                    + "Reservation ID    : " + reservationId + "  →  Confirmed\n\n"
                    + "Click '⬇ Download Receipt' to save your receipt.",
                    "Payment Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
            txRefField.setText("");
        }

        loadPayments();
    }

    // ── Show Transaction Details dialog ───────────────────────────────────────
    private void showTransactionDetails() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Select a payment row first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Object pidObj = model.getValueAt(row, 0);
        if ("-".equals(pidObj.toString())) {
            JOptionPane.showMessageDialog(this,
                    "This reservation has not been paid yet.\n"
                    + "Enter the Telebirr reference and click 'Verify & Pay'.",
                    "Not Paid", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int paymentId;
        try {
            paymentId = Integer.parseInt(pidObj.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid payment ID.");
            return;
        }

        Payment payment = paymentService.getPaymentById(paymentId);
        if (payment == null) {
            JOptionPane.showMessageDialog(this, "Payment record not found.");
            return;
        }

        // Try cache first, then attempt API re-verify only if not cached
        TelebirrService.TelebirrTransactionDetails tx = txCache.get(paymentId);
        if (tx == null && payment.getTransactionRef() != null && !payment.getTransactionRef().isEmpty()) {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Would you like to re-fetch live transaction details from Telebirr?\n"
                    + "(Reference: " + payment.getTransactionRef() + ")",
                    "Fetch Live Details?", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                TelebirrService svc = new TelebirrService();
                TelebirrService.VerificationResult result = svc.verifyTelebirrPayment(payment.getTransactionRef());
                setCursor(Cursor.getDefaultCursor());
                if (result.success) {
                    tx = result.data;
                    txCache.put(paymentId, tx); // cache it
                }
            }
        }

        if (tx != null && "Simulated".equals(tx.settledAmount)) {
            tx.settledAmount = String.format("%,.2f Birr", payment.getAmount());
            tx.totalPaidAmount = String.format("%,.2f Birr", payment.getAmount());
        }

        // Build details message
        StringBuilder sb = new StringBuilder();
        sb.append("── Payment Record ───────────────────────\n");
        sb.append(String.format("Payment ID        : %d%n", payment.getPaymentId()));
        sb.append(String.format("Reservation ID    : %d%n", payment.getReservationId()));
        sb.append(String.format("Customer          : %s%n", payment.getCustomerName()));
        sb.append(String.format("Room Type         : %s%n", payment.getRoomType() != null ? payment.getRoomType() : "N/A"));
        sb.append(String.format("Amount            : ETB %,.2f%n", payment.getAmount()));
        sb.append(String.format("Method            : %s%n", payment.getPaymentMethod()));
        sb.append(String.format("Status            : %s%n", payment.getStatus()));
        sb.append(String.format("Reference         : %s%n", payment.getTransactionRef() != null ? payment.getTransactionRef() : "N/A"));
        sb.append(String.format("Date              : %s%n",
                payment.getCreatedAt() != null ? payment.getCreatedAt().toLocalDate().toString() : "N/A"));

        if (tx != null) {
            sb.append("\n── Telebirr Transaction Details ─────────\n");
            sb.append(String.format("Receipt No        : %s%n", tx.receiptNo));
            sb.append(String.format("Payer             : %s%n", tx.payerName));
            sb.append(String.format("Payer Phone       : %s%n", tx.payerTelebirrNo));
            sb.append(String.format("Credited To       : %s%n", tx.creditedPartyName));
            sb.append(String.format("Tx Status         : %s%n", tx.transactionStatus));
            sb.append(String.format("Settled Amount    : %s%n", tx.settledAmount));
            sb.append(String.format("Service Fee (VAT) : %s%n", tx.serviceFeeVAT));
            sb.append(String.format("Total Paid        : %s%n", tx.totalPaidAmount));
            sb.append(String.format("Payment Date      : %s%n", tx.paymentDate));
        } else {
            sb.append("\n(Telebirr live details not available — use Download Receipt to view full info)");
        }

        // Show in a scrollable dialog
        JTextArea area = new JTextArea(sb.toString());
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));
        area.setEditable(false);
        area.setBackground(new Color(0xF5F5F5));
        area.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(480, 320));
        JOptionPane.showMessageDialog(this, scroll,
                "Transaction Details — Payment #" + paymentId,
                JOptionPane.INFORMATION_MESSAGE);
    }

    // ── Download Receipt ──────────────────────────────────────────────────────
    private void downloadReceipt() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Select a paid payment row first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Object pidObj = model.getValueAt(row, 0);
        if ("-".equals(pidObj.toString())) {
            JOptionPane.showMessageDialog(this,
                    "This reservation hasn't been paid yet. Pay first, then download the receipt.",
                    "Not Paid", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Object statusObj = model.getValueAt(row, 4);
        if (statusObj == null || !("Paid".equalsIgnoreCase(statusObj.toString()))) {
            JOptionPane.showMessageDialog(this,
                    "Receipt is only available for Paid payments.",
                    "Not Paid", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int paymentId;
        try {
            paymentId = Integer.parseInt(pidObj.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid payment ID.");
            return;
        }

        Payment payment = paymentService.getPaymentById(paymentId);
        if (payment == null) {
            JOptionPane.showMessageDialog(this, "Payment record not found.");
            return;
        }

        // Use cached Telebirr details — no second API call needed
        TelebirrService.TelebirrTransactionDetails tx = txCache.get(paymentId);

        // If not in cache (e.g. app restarted) try a silent re-verify
        if (tx == null && payment.getTransactionRef() != null && !payment.getTransactionRef().isEmpty()) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            TelebirrService svc = new TelebirrService();
            TelebirrService.VerificationResult result = svc.verifyTelebirrPayment(payment.getTransactionRef());
            setCursor(Cursor.getDefaultCursor());
            if (result.success) {
                tx = result.data;
                txCache.put(paymentId, tx);
            }
            // If re-verify fails, tx stays null — receipt still generates from DB data
        }

        if (tx != null && "Simulated".equals(tx.settledAmount)) {
            tx.settledAmount = String.format("%,.2f Birr", payment.getAmount());
            tx.totalPaidAmount = String.format("%,.2f Birr", payment.getAmount());
        }

        String receiptText = ReceiptGenerator.generate(payment, tx);
        saveReceiptFile(receiptText, "receipt-payment-" + paymentId);
    }

    // ── Save to file ──────────────────────────────────────────────────────────
    private void saveReceiptFile(String content, String defaultName) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save Receipt");
        chooser.setSelectedFile(new File(defaultName + ".txt"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try (FileWriter fw = new FileWriter(file)) {
                fw.write(content);
                JOptionPane.showMessageDialog(this,
                        "✅ Receipt saved to:\n" + file.getAbsolutePath(),
                        "Saved", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Could not save receipt: " + ex.getMessage(),
                        "Save Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
