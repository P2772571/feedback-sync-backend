package com.example.feedbacksync.entity;

import com.example.feedbacksync.entity.enums.PipOutcome;
import com.example.feedbacksync.entity.enums.PipStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "pips")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Pip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pipId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(name = "progress", nullable = false)
    private Integer progress = 0;

    @Column(name = "support", nullable = false)
    private String support;

    @Enumerated(EnumType.STRING)
    @Column(name = "outcome")
    private PipOutcome outcome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false )
    private PipStatus status = PipStatus.ACTIVE;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Relationships

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private User employee;

    @ManyToOne
    @JoinColumn(name = "manager_id", nullable = false)
    private User manager;

}
