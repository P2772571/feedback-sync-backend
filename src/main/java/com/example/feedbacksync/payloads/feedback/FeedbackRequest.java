package com.example.feedbacksync.payloads.feedback;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackRequest {
    private String content;
    private Boolean isSharedWithManager;
    private Long giverId;
    private Long receiverId;
}
