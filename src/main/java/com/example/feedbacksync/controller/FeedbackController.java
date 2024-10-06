package com.example.feedbacksync.controller;

import com.example.feedbacksync.payloads.feedback.FeedbackRequest;
import com.example.feedbacksync.payloads.feedback.FeedbackResponse;
import com.example.feedbacksync.service.FeedbackService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin(origins = "http://localhost:5173")
public class FeedbackController {

    private final FeedbackService feedbackService;

    /**
     * Constructor for FeedbackController
     * @param feedbackService FeedbackService
     */
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    /**
     * Create a new feedback for a user
     * @param request FeedbackRequest
     * @return FeedbackResponse
     */
    @PostMapping
    public ResponseEntity<?> createFeedback(@RequestBody FeedbackRequest request) {
        try {
            FeedbackResponse response = feedbackService.createFeedback(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);  
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get all feedbacks for a user
     * @param userId - The user id
     * @return List of FeedbackResponse
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAllFeedbacksOfUser(@PathVariable Long userId) {  
        try {
            List<FeedbackResponse> receivedFeedbacks = feedbackService.getAllFeedbacksByReceiver(userId);
            List<FeedbackResponse> givenFeedbacks = feedbackService.getAllFeedbacksByGiver(userId);
            List<FeedbackResponse> feedbacksSharedWithManager = feedbackService.getAllFeedbacksSharedWithManager(userId);

            Map<String, List<FeedbackResponse>> response = Map.of(
                    "received", receivedFeedbacks,
                    "given", givenFeedbacks,
                    "shared", feedbacksSharedWithManager
            );

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get feedback by feedback id
     * @param feedbackId Long feedback id
     * @return FeedbackResponse or null
     */
    @GetMapping("/{feedbackId}")
    public ResponseEntity<?> getFeedbackById(@PathVariable Long feedbackId) {  
        try {
            FeedbackResponse response = feedbackService.getFeedbackById(feedbackId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Update feedback by feedback id
     * @param feedbackId Long feedback id
     * @param request FeedbackRequest
     * @return FeedbackResponse or null
     */
    @PutMapping("/{feedbackId}")
    public ResponseEntity<?> updateFeedbackById(@PathVariable Long feedbackId, @RequestBody FeedbackRequest request) {  
        try {
            FeedbackResponse response = feedbackService.updateFeedback(feedbackId, request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
