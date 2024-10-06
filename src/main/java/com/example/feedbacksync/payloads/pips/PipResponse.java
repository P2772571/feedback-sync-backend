package com.example.feedbacksync.payloads.pips;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.feedbacksync.entity.enums.PipOutcome;
import com.example.feedbacksync.entity.enums.PipStatus;
import com.example.feedbacksync.payloads.task.TaskResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PipResponse {
    private Long pipId;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String support;
    private Integer progress = 0;
    private PipOutcome outcome;
    private PipStatus status;
    private String managerName;
    private String employeeName;
    private List<TaskResponse> tasks = new ArrayList<>();
    
}
