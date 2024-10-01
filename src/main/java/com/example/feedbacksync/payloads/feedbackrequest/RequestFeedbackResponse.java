package com.example.feedbacksync.payloads.feedbackrequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestFeedbackResponse {
    private Long requestId;          // ID of the feedback request
    private String message;          // Message or description
    private String requesterName;    // Name of the requester
    private String requesteeName;    // Name of the requestee
    private String status;           // Status of the request (PENDING, COMPLETED)
    private LocalDateTime createdAt; // Timestamp when the request was created
}
