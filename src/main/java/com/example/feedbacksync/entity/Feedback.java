package com.example.feedbacksync.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "feedback")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedbackId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(nullable = false)
    private Boolean isSharedWithManager = false;


    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "giver_id", nullable = false)
    private User giver;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;



}
