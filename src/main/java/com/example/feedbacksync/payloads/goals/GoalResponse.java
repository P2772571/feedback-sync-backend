package com.example.feedbacksync.payloads.goals;

import com.example.feedbacksync.entity.enums.GoalStatus;
import com.example.feedbacksync.payloads.task.TaskResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoalResponse {
    private Long goalId;
    private String goalName;
    private String description;
    private Integer progress=0;
    private GoalStatus status = GoalStatus.PENDING;
    private LocalDateTime dueDate;
    private String mangerName;
    private String employeeName;
    private List<TaskResponse> tasks = new ArrayList<>();

}
