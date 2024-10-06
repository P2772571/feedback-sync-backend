package com.example.feedbacksync.payloads.feedbackrequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestFeedbackRequest {
    private Long requesterId;   // ID of the user requesting feedback
    private Long requesteeId;   // ID of the user providing feedback
    private String message;     // Message or details of the feedback request
}
