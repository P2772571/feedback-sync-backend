package com.example.feedbacksync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.feedbacksync.entity.Task;

import com.example.feedbacksync.entity.Goal;
import com.example.feedbacksync.entity.Pip;

import java.util.List;

/**
 * TaskRepository is an interface that extends JpaRepository.
 * It provides methods to perform CRUD operations on the Task table in the
 * database.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByGoal(Goal goal);
    List<Task> findAllByPip(Pip pip);
}
