package com.ruyahotel.service;

import com.ruyahotel.dao.*;

import java.util.HashMap;
import java.util.Map;

public class ReportService {
    private final UserDAO userDAO = new UserDAO();
    private final RoomDAO roomDAO = new RoomDAO();
    private final ReservationDAO reservationDAO = new ReservationDAO();
    private final FeedbackDAO feedbackDAO = new FeedbackDAO();

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userDAO.getAll().size());
        stats.put("totalRooms", roomDAO.count());
        stats.put("totalReservations", reservationDAO.count());
        stats.put("totalRevenue", reservationDAO.getTotalRevenue());
        stats.put("todayCheckIns", reservationDAO.getTodayCheckIns());
        stats.put("todayCheckOuts", reservationDAO.getTodayCheckOuts());
        stats.put("availableRooms", roomDAO.countByStatus("Available"));
        stats.put("occupiedRooms", roomDAO.countByStatus("Occupied"));
        stats.put("maintenanceRooms", roomDAO.countByStatus("Maintenance"));
        stats.put("pendingReservations", reservationDAO.countByStatus("Pending"));
        stats.put("confirmedReservations", reservationDAO.countByStatus("Confirmed"));
        stats.put("checkedInReservations", reservationDAO.countByStatus("Checked-In"));
        stats.put("cancelledReservations", reservationDAO.countByStatus("Cancelled"));
        stats.put("expiredReservations", reservationDAO.countByStatus("Expired"));
        stats.put("averageRating", feedbackDAO.getAverageRating());
        return stats;
    }

    public Map<String, Integer> getBookingStatusDistribution() {
        Map<String, Integer> map = new HashMap<>();
        map.put("Confirmed", reservationDAO.countByStatus("Confirmed"));
        map.put("Pending", reservationDAO.countByStatus("Pending"));
        map.put("Checked-In", reservationDAO.countByStatus("Checked-In"));
        map.put("Cancelled", reservationDAO.countByStatus("Cancelled"));
        map.put("Expired", reservationDAO.countByStatus("Expired"));
        return map;
    }

    public Map<String, Integer> getRoomStatusDistribution() {
        Map<String, Integer> map = new HashMap<>();
        map.put("Available", roomDAO.countByStatus("Available"));
        map.put("Reserved", roomDAO.countByStatus("Reserved"));
        map.put("Occupied", roomDAO.countByStatus("Occupied"));
        map.put("Maintenance", roomDAO.countByStatus("Maintenance"));
        return map;
    }
}
