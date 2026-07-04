package com.ruyahotel.util;

import com.ruyahotel.model.Payment;
import com.ruyahotel.service.TelebirrService;

import java.time.LocalDate;

public class ReceiptGenerator {

    private static final String LINE  = "  ───────────────────────────────────────────────────";
    private static final String DLINE = "  ═══════════════════════════════════════════════════";

    /**
     * Generates a formatted text receipt.
     * @param payment  Payment record from DB (required)
     * @param tx       Telebirr transaction details (may be null – DB data only shown)
     * @return         Formatted receipt string ready to write to a file
     */
    public static String generate(Payment payment, TelebirrService.TelebirrTransactionDetails tx) {
        StringBuilder sb = new StringBuilder();

        sb.append("╔═════════════════════════════════════════════════════╗\n");
        sb.append("║               RUYA HOTEL MANAGEMENT                 ║\n");
        sb.append("║             OFFICIAL PAYMENT RECEIPT                ║\n");
        sb.append("╚═════════════════════════════════════════════════════╝\n\n");

        // ── Booking Info ──────────────────────────────────────────────────────
        sb.append(LINE).append("\n");
        sb.append("  BOOKING INFORMATION\n");
        sb.append(LINE).append("\n");
        row(sb, "Payment ID",     String.valueOf(payment.getPaymentId()));
        row(sb, "Reservation ID", String.valueOf(payment.getReservationId()));
        row(sb, "Customer",       payment.getCustomerName());
        row(sb, "Room Type",      payment.getRoomType() != null ? payment.getRoomType() : "N/A");
        row(sb, "Amount",         "ETB " + String.format("%,.2f", payment.getAmount()));
        row(sb, "Method",         payment.getPaymentMethod());
        row(sb, "Status",         payment.getStatus());
        row(sb, "Reference",      payment.getTransactionRef() != null ? payment.getTransactionRef() : "N/A");
        String date = payment.getCreatedAt() != null
                ? payment.getCreatedAt().toLocalDate().toString()
                : LocalDate.now().toString();
        row(sb, "Date",           date);
        sb.append("\n");

        // ── Telebirr Details (if available) ───────────────────────────────────
        if (tx != null) {
            sb.append(LINE).append("\n");
            sb.append("  TELEBIRR TRANSACTION DETAILS\n");
            sb.append(LINE).append("\n");
            row(sb, "Receipt No",       tx.receiptNo);
            row(sb, "Payer Name",       tx.payerName);
            row(sb, "Payer Phone",      tx.payerTelebirrNo);
            row(sb, "Credited To",      tx.creditedPartyName);
            row(sb, "Account No",       tx.creditedPartyAccountNo);
            row(sb, "Tx Status",        tx.transactionStatus);
            row(sb, "Settled Amount",   tx.settledAmount);
            row(sb, "Service Fee+VAT",  tx.serviceFeeVAT);
            row(sb, "Total Paid",       tx.totalPaidAmount);
            row(sb, "Payment Date",     tx.paymentDate);
            sb.append("\n");
        }

        // ── Footer ─────────────────────────────────────────────────────────────
        sb.append(DLINE).append("\n");
        sb.append("  Thank you for choosing Ruya Hotel!\n");
        sb.append("  We look forward to welcoming you again.\n");
        sb.append(DLINE).append("\n");

        return sb.toString();
    }

    private static void row(StringBuilder sb, String label, String value) {
        sb.append(String.format("  %-22s: %s%n", label, value != null ? value : "N/A"));
    }
}
