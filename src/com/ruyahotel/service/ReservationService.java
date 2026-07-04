package com.ruyahotel.service;

import com.ruyahotel.dao.ReservationDAO;
import com.ruyahotel.dao.RoomDAO;
import com.ruyahotel.model.Reservation;
import com.ruyahotel.model.Room;

import java.time.LocalDate;
import java.util.List;

public class ReservationService {
    private final ReservationDAO reservationDAO = new ReservationDAO();
    private final RoomDAO roomDAO = new RoomDAO();

    public int createReservation(Reservation r) {
        if (!ValidationService.isNotEmpty(r.getFullName())) return -1;
        if (!ValidationService.isValidEmail(r.getEmail())) return -1;
        if (!ValidationService.isValidPhone(r.getPhone())) return -1;
        if (!ValidationService.isValidDateRange(r.getCheckInDate(), r.getCheckOutDate())) return -1;
        if (r.getCheckInDate().isBefore(LocalDate.now())) return -1;

        Room room = roomDAO.getById(r.getRoomId());
        if (room == null) return -1;
        if (!"Available".equals(room.getStatus())) return -1;

        r.calculateTotals(room.getPricePerNight());
        r.setStatus("Pending");
        r.setPaymentStatus("Pending");

        int reservationId = reservationDAO.create(r);
        if (reservationId > 0) {
            roomDAO.updateStatus(r.getRoomId(), "Reserved");
        }
        return reservationId;
    }

    public String cancelReservation(int reservationId, int userId) {
        Reservation r = reservationDAO.getById(reservationId);
        if (r == null) return "Reservation not found";
        if (r.getUserId() != userId) return "Unauthorized";
        if (!r.canCancel()) return "Cannot cancel this reservation";

        boolean ok = reservationDAO.cancel(reservationId);
        if (ok) {
            roomDAO.updateStatus(r.getRoomId(), "Available");
        }
        return ok ? null : "Cancellation failed";
    }

    public boolean adminCancel(int reservationId) {
        Reservation r = reservationDAO.getById(reservationId);
        if (r == null) return false;
        boolean ok = reservationDAO.cancel(reservationId);
        if (ok) roomDAO.updateStatus(r.getRoomId(), "Available");
        return ok;
    }

    public boolean checkIn(int reservationId) {
        Reservation r = reservationDAO.getById(reservationId);
        if (r == null || !"Confirmed".equals(r.getStatus())) return false;
        boolean ok = reservationDAO.updateStatus(reservationId, "Checked-In");
        if (ok) roomDAO.updateStatus(r.getRoomId(), "Occupied");
        return ok;
    }

    public boolean checkOut(int reservationId) {
        Reservation r = reservationDAO.getById(reservationId);
        if (r == null || !"Checked-In".equals(r.getStatus())) return false;
        boolean ok = reservationDAO.updateStatus(reservationId, "Checked-Out");
        if (ok) roomDAO.updateStatus(r.getRoomId(), "Available");
        return ok;
    }

    public boolean confirmReservation(int reservationId) {
        return reservationDAO.updateStatus(reservationId, "Confirmed");
    }

    public List<Reservation> getUserReservations(int userId) {
        return reservationDAO.getByUser(userId);
    }

    public List<Reservation> getAllReservations() {
        reservationDAO.expireOldReservations();
        return reservationDAO.getAll();
    }

    public List<Reservation> getByStatus(String status) {
        return reservationDAO.getByStatus(status);
    }

    public Reservation getById(int id) {
        return reservationDAO.getById(id);
    }
}
