package com.example.feedbacksync.repository;

import com.example.feedbacksync.entity.FeedbackRequest;
import com.example.feedbacksync.entity.User;
import com.example.feedbacksync.entity.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRequestRepository extends JpaRepository<FeedbackRequest, Long> {

    // Find all feedback requests by requester (the user who requested feedback)
    List<FeedbackRequest> findAllByRequester(User requester);

    // Find all feedback requests by requestee (the user providing feedback)
    List<FeedbackRequest> findAllByRequesteeAndStatus(User requestee, RequestStatus status);

    // Find all feedback requests with a specific status (e.g., PENDING, COMPLETED)
    List<FeedbackRequest> findAllByStatus(RequestStatus status);
}
