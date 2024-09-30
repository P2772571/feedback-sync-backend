package com.example.feedbacksync.repository;

import com.example.feedbacksync.entity.Feedback;
import com.example.feedbacksync.entity.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback>  findAllByGiver(User giver);
    List<Feedback> findAllByReceiver(User receiver);
    List<Feedback> findAllByReceiverAndIsSharedWithManager(User receiver, Boolean isSharedWithManager);
}
