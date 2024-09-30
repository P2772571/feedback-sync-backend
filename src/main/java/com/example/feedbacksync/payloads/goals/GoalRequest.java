package com.example.feedbacksync.payloads.goals;

import com.example.feedbacksync.entity.enums.GoalStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.example.feedbacksync.payloads.task.TaskRequest;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GoalRequest {
    private String goalName;
    private String description;
    private LocalDateTime dueDate;
    private GoalStatus status ;
    private Integer progress ;
    private Long userId;
    private Long assignedById;
    private List<TaskRequest> tasks = new ArrayList<>();

}
