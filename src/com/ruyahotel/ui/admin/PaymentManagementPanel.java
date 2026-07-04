package com.ruyahotel.ui.admin;

import com.ruyahotel.model.Payment;
import com.ruyahotel.service.PaymentService;
import com.ruyahotel.service.TelebirrService;
import com.ruyahotel.ui.components.ModernTable;
import com.ruyahotel.ui.components.ModernTheme;
import com.ruyahotel.ui.components.RoundedButton;
import com.ruyahotel.ui.components.RoundedPanel;
import com.ruyahotel.util.ReceiptGenerator;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PaymentManagementPanel extends JPanel {
    private final Consumer<String> navigator;
    private final PaymentService paymentService = new PaymentService();
    private ModernTable table;
    private DefaultTableModel model;

    public PaymentManagementPanel(Consumer<String> navigator) {
        this.navigator = navigator;
        setLayout(new BorderLayout());
        setBackground(ModernTheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("Payment Management");
        title.setFont(ModernTheme.FONT_HEADING);
        title.setForeground(ModernTheme.TEXT_PRIMARY);
        add(title, BorderLayout.NORTH);

        String[] cols = {"Payment ID", "Reservation", "Customer", "Amount (ETB)", "Method", "Status", "Date"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new ModernTable(model);
        add(table.wrapInScrollPane(), BorderLayout.CENTER);

        // ── Bottom buttons ─────────────────────────────────────────────────────
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        bottom.setOpaque(false);

        RoundedButton verifyBtn = new RoundedButton("✔ Mark as Paid", ModernTheme.SUCCESS);
        verifyBtn.addActionListener(e -> markSelectedPaid());
        bottom.add(verifyBtn);

        RoundedButton detailsBtn = new RoundedButton("🔍 View Details", ModernTheme.INFO);
        detailsBtn.addActionListener(e -> showPaymentDetails());
        bottom.add(detailsBtn);

        RoundedButton downloadBtn = new RoundedButton("⬇ Download Receipt", ModernTheme.PRIMARY);
        downloadBtn.addActionListener(e -> downloadSelectedReceipt());
        bottom.add(downloadBtn);

        RoundedButton refreshBtn = new RoundedButton("↻ Refresh", ModernTheme.INFO);
        refreshBtn.addActionListener(e -> loadPayments());
        bottom.add(refreshBtn);

        add(bottom, BorderLayout.SOUTH);
        loadPayments();
    }

    // ── Load table ────────────────────────────────────────────────────────────
    private void loadPayments() {
        model.setRowCount(0);
        List<Payment> list = paymentService.getAllPayments();
        for (Payment p : list) {
            model.addRow(new Object[]{
                    p.getPaymentId(), p.getReservationId(), p.getCustomerName(),
                    String.format("%,.2f", p.getAmount()), p.getPaymentMethod(),
                    p.getStatus(),
                    p.getCreatedAt() != null ? p.getCreatedAt().toLocalDate().toString() : ""
            });
        }
    }

    // ── Mark as Paid ──────────────────────────────────────────────────────────
    private void markSelectedPaid() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a payment record first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int pid = (int) model.getValueAt(row, 0);
        paymentService.confirmPayment(pid);
        JOptionPane.showMessageDialog(this, "Payment #" + pid + " marked as Paid.", "Done", JOptionPane.INFORMATION_MESSAGE);
        loadPayments();
    }

    // ── View Details ──────────────────────────────────────────────────────────
    private void showPaymentDetails() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a payment record.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int pid = (int) model.getValueAt(row, 0);
        Payment payment = paymentService.getPaymentById(pid);
        if (payment == null) {
            JOptionPane.showMessageDialog(this, "Payment not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Build base details
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("  %-22s: %s%n", "Payment ID",      payment.getPaymentId()));
        sb.append(String.format("  %-22s: %s%n", "Reservation ID",  payment.getReservationId()));
        sb.append(String.format("  %-22s: %s%n", "Customer",        payment.getCustomerName()));
        sb.append(String.format("  %-22s: %s%n", "Room Type",       payment.getRoomType() != null ? payment.getRoomType() : "N/A"));
        sb.append(String.format("  %-22s: ETB %s%n", "Amount",      String.format("%,.2f", payment.getAmount())));
        sb.append(String.format("  %-22s: %s%n", "Method",          payment.getPaymentMethod()));
        sb.append(String.format("  %-22s: %s%n", "Status",          payment.getStatus()));
        sb.append(String.format("  %-22s: %s%n", "Transaction Ref", payment.getTransactionRef() != null ? payment.getTransactionRef() : "N/A"));
        sb.append(String.format("  %-22s: %s%n", "Date",            payment.getCreatedAt() != null ? payment.getCreatedAt().toLocalDate() : "N/A"));
        sb.append(String.format("  %-22s: %s%n", "Paid At",         payment.getPaidAt() != null ? payment.getPaidAt().toLocalDate() : "N/A"));

        // Re-verify Telebirr for full live details
        if ("Telebirr".equalsIgnoreCase(payment.getPaymentMethod()) && payment.getTransactionRef() != null) {
            sb.append("\n  ───────────────────────────────────────────────────\n");
            sb.append("  TELEBIRR TRANSACTION DETAILS\n");
            sb.append("  ───────────────────────────────────────────────────\n");

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            TelebirrService svc = new TelebirrService();
            TelebirrService.VerificationResult result = svc.verifyTelebirrPayment(payment.getTransactionRef());
            setCursor(Cursor.getDefaultCursor());

            if (result.success) {
                TelebirrService.TelebirrTransactionDetails tx = result.data;
                if ("Simulated".equals(tx.settledAmount)) {
                    tx.settledAmount = String.format("%,.2f Birr", payment.getAmount());
                    tx.totalPaidAmount = String.format("%,.2f Birr", payment.getAmount());
                }
                sb.append(String.format("  %-22s: %s%n", "Receipt No",       tx.receiptNo));
                sb.append(String.format("  %-22s: %s%n", "Payer Name",       tx.payerName));
                sb.append(String.format("  %-22s: %s%n", "Payer Phone",      tx.payerTelebirrNo));
                sb.append(String.format("  %-22s: %s%n", "Credited To",      tx.creditedPartyName));
                sb.append(String.format("  %-22s: %s%n", "Account No",       tx.creditedPartyAccountNo));
                sb.append(String.format("  %-22s: %s%n", "Tx Status",        tx.transactionStatus));
                sb.append(String.format("  %-22s: %s%n", "Settled Amount",   tx.settledAmount));
                sb.append(String.format("  %-22s: %s%n", "Service Fee+VAT",  tx.serviceFeeVAT));
                sb.append(String.format("  %-22s: %s%n", "Total Paid",       tx.totalPaidAmount));
                sb.append(String.format("  %-22s: %s%n", "Payment Date",     tx.paymentDate));
            } else {
                sb.append("  Could not fetch live Telebirr details:\n");
                sb.append("  ").append(result.errorMessage).append("\n");
                sb.append("  (Transaction ref stored: ").append(payment.getTransactionRef()).append(")\n");
            }
        }

        // Show in a scrollable dialog
        JTextArea area = new JTextArea(sb.toString());
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));
        area.setEditable(false);
        area.setBackground(ModernTheme.CARD_BG);
        area.setForeground(ModernTheme.TEXT_PRIMARY);
        area.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(540, 380));
        scroll.setBorder(null);

        JOptionPane.showMessageDialog(this, scroll, "Payment Details — #" + pid, JOptionPane.PLAIN_MESSAGE);
    }

    // ── Download Receipt ──────────────────────────────────────────────────────
    private void downloadSelectedReceipt() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a payment record.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int pid = (int) model.getValueAt(row, 0);
        Payment payment = paymentService.getPaymentById(pid);
        if (payment == null) {
            JOptionPane.showMessageDialog(this, "Payment not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Optionally fetch live Telebirr details
        TelebirrService.TelebirrTransactionDetails tx = null;
        if ("Telebirr".equalsIgnoreCase(payment.getPaymentMethod()) && payment.getTransactionRef() != null) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            TelebirrService svc = new TelebirrService();
            TelebirrService.VerificationResult result = svc.verifyTelebirrPayment(payment.getTransactionRef());
            setCursor(Cursor.getDefaultCursor());
            if (result.success) {
                tx = result.data;
                if ("Simulated".equals(tx.settledAmount)) {
                    tx.settledAmount = String.format("%,.2f Birr", payment.getAmount());
                    tx.totalPaidAmount = String.format("%,.2f Birr", payment.getAmount());
                }
            }
        }

        String receiptText = ReceiptGenerator.generate(payment, tx);
        saveReceiptFile(receiptText, "receipt-payment-" + pid);
    }

    // ── Save file ─────────────────────────────────────────────────────────────
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
