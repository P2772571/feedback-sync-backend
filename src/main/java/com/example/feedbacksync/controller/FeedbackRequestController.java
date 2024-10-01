package com.example.feedbacksync.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.feedbacksync.payloads.feedbackrequest.RequestFeedbackRequest;
import com.example.feedbacksync.service.FeedbackRequestService;

@RestController
@RequestMapping("/api/feedback-request")
@CrossOrigin(origins = "http://localhost:5173")
public class FeedbackRequestController {

    private final FeedbackRequestService feedbackRequestService;

    /**
     * Constructor for FeedbackRequestController
     * @param feedbackRequestService FeedbackRequestService
     */
    FeedbackRequestController(FeedbackRequestService feedbackRequestService) {
        this.feedbackRequestService = feedbackRequestService;
    }
    

    /**
     * Create a new feedback request
     * @param request RequestFeedbackRequest
     * @return ResponseEntity<?>
     */
    @PostMapping
    public ResponseEntity<?> createFeedbackRequest(@RequestBody RequestFeedbackRequest request) {
        try {
            return new ResponseEntity<>(feedbackRequestService.createFeedbackRequest(request), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{feedbackRequestId}")
    public ResponseEntity<?> updateFeedbackRequest(@PathVariable Long feedbackRequestId) {
        try {
            return new ResponseEntity<>(feedbackRequestService.update(feedbackRequestId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get all feedback requests for a user
     * @param userId - The user id
     * @return List of FeedbackRequestResponse
     */
    @GetMapping("/user/{userId}/received")
        public ResponseEntity<?> getAllFeedbackRequestsOfUser(@PathVariable Long userId) {
        try {
            return new ResponseEntity<>(feedbackRequestService.getFeedbackRequestOfUser(userId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get all feedback requests by a user
     * @param userId - The user id
     * @return List of FeedbackRequestResponse
     */
    @GetMapping("/user/{userId}/sent")
    public ResponseEntity<?> getAllFeedbackRequestsByUser(@PathVariable Long userId) {
        try {
            return new ResponseEntity<>(feedbackRequestService.getFeedbackRequestMadeByUser(userId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get feedback request by feedback request id
     * @param feedbackRequestId - The feedback request id
     * @return FeedbackRequestResponse
     */
    @GetMapping("/{feedbackRequestId}")
    public ResponseEntity<?> getFeedbackRequestById(@PathVariable Long feedbackRequestId) {
        try {
            return new ResponseEntity<>(feedbackRequestService.getFeedbackRequestById(feedbackRequestId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    

    
}
