package com.example.feedbacksync.entity;

import com.example.feedbacksync.entity.enums.PipOutcome;
import com.example.feedbacksync.entity.enums.PipStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "pip")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Pip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pipId;

    @Column(name = "objectives", nullable = false)
    private String objectives;

    @Column(name = "timeline", nullable = false)
    private String timeline;

    @Column(name = "support", nullable = false)
    private String support;

    @Column(name = "outcome", nullable = false)
    private PipOutcome outcome;

    @Column(name = "status", nullable = false)
    private PipStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships

}
