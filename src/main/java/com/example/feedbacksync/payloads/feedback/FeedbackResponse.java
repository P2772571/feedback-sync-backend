package com.example.feedbacksync.payloads.feedback;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResponse {
    private Long feedbackId;
    private String content;
    private Boolean isSharedWithManager;
    private LocalDateTime createdAt;
    private String receiverName;
    private String giverName;

}
