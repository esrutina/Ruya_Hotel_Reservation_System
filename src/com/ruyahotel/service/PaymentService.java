package com.ruyahotel.service;

import com.ruyahotel.dao.PaymentDAO;
import com.ruyahotel.dao.ReservationDAO;
import com.ruyahotel.model.Payment;
import com.ruyahotel.model.Reservation;

import java.util.List;

public class PaymentService {

    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final ReservationDAO reservationDAO = new ReservationDAO();

    public Payment processPayment(int reservationId, int userId, String method, String txRef) {

        Reservation r = reservationDAO.getById(reservationId);

        if (r == null) return null;
        if (r.getUserId() != userId) return null;
        if ("Cancelled".equals(r.getStatus()) || "Expired".equals(r.getStatus())) return null;

        // If already fully paid, nothing to do
        Payment existing = paymentDAO.getByReservation(reservationId);
        if (existing != null && existing.isPaid()) return null;

        // If a pending payment record exists, update its txRef and reuse it
        if (existing != null) {
            paymentDAO.updateTransactionRef(existing.getPaymentId(), txRef);
            existing.setTransactionRef(txRef);
            return existing;
        }

        Payment p = new Payment();
        p.setReservationId(reservationId);
        p.setUserId(userId);
        p.setAmount(r.getTotalPrice());
        p.setPaymentMethod(method);
        p.setStatus("Pending");
        p.setTransactionRef(txRef);

        boolean ok = paymentDAO.create(p);
        // After create(), p.getPaymentId() is set via RETURN_GENERATED_KEYS
        return ok ? p : null;
    }

    public String confirmPayment(int paymentId) {
        Payment p = paymentDAO.getById(paymentId);
        if (p == null) return "Payment not found";
        if (p.isPaid()) return "Already paid";

        boolean ok = paymentDAO.markPaid(paymentId);

        if (ok) {
            reservationDAO.updateStatus(p.getReservationId(), "Confirmed");
            reservationDAO.updatePaymentStatus(p.getReservationId(), "Paid");
        }

        return ok ? null : "Payment confirmation failed";
    }

    public Payment getPaymentById(int paymentId) {
        return paymentDAO.getById(paymentId);
    }

    public Payment getByReservation(int reservationId) {
        return paymentDAO.getByReservation(reservationId);
    }

    public List<Payment> getUserPayments(int userId) {
        return paymentDAO.getByUser(userId);
    }

    public List<Payment> getAllPayments() {
        return paymentDAO.getAll();
    }
}