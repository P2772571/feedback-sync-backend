package com.example.feedbacksync.payloads.pips;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.feedbacksync.entity.enums.PipOutcome;
import com.example.feedbacksync.entity.enums.PipStatus;
import com.example.feedbacksync.payloads.task.TaskRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PipRequest {
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String support;
    private PipOutcome outcome;
    private Long employeeId;
    private Long managerId;
    private PipStatus status;
    private List<TaskRequest> tasks = new ArrayList<>();
    
}
