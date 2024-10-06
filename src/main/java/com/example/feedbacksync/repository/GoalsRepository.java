package com.example.feedbacksync.repository;

import com.example.feedbacksync.entity.Goal;
import com.example.feedbacksync.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoalsRepository extends JpaRepository<Goal, Long> {

    List<Goal> findAllByUser(User user);
    List<Goal> findAllByAssignedBy(User user);
    List<Goal> findAllByAssignedByAndUser(User assignedBy, User user);
}
