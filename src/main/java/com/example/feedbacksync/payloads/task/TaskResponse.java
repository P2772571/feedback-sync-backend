package com.example.feedbacksync.payloads.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private Long taskId;
    private String taskName;
    private Boolean isCompleted;
    private Long goalId;
    private Long pipId;
}
