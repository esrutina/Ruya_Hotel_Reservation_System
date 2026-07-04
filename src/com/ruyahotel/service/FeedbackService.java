package com.ruyahotel.service;

import com.ruyahotel.dao.FeedbackDAO;
import com.ruyahotel.model.Feedback;

import java.util.List;

public class FeedbackService {
    private final FeedbackDAO feedbackDAO = new FeedbackDAO();

    public String submitFeedback(Feedback f) {
        if (f.getRating() < 1 || f.getRating() > 5) return "Rating must be between 1 and 5";
        if (!ValidationService.isNotEmpty(f.getComment())) return "Comment is required";
        if (feedbackDAO.hasFeedbackForReservation(f.getReservationId())) return "Feedback already submitted for this reservation";
        return feedbackDAO.create(f) ? null : "Failed to submit feedback";
    }

    public List<Feedback> getAll() {
        return feedbackDAO.getAll();
    }

    public List<Feedback> getByUser(int userId) {
        return feedbackDAO.getByUser(userId);
    }

    public boolean deleteFeedback(int feedbackId) {
        return feedbackDAO.softDelete(feedbackId);
    }

    public double getAverageRating() {
        return feedbackDAO.getAverageRating();
    }
}
