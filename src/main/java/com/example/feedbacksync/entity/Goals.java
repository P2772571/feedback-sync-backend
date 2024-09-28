package com.example.feedbacksync.entity;

import com.example.feedbacksync.entity.enums.GoalStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "goals")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Goals {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goalId;

    @Column(name = "goal_name", nullable = false)
    private String goalName;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(columnDefinition = "int default 0")
    private Integer progress;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private GoalStatus status = GoalStatus.PENDING;

    @Column(name = "due_date", nullable = false)
    private Date dueDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();


     @ManyToOne
     @JoinColumn(name = "user_id", nullable = false)
     private User user;

     @ManyToOne
     @JoinColumn(name = "assigned_by")
     private User assignedBy;
}
