package com.example.feedbacksync.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.feedbacksync.entity.Pip;
import com.example.feedbacksync.entity.User;

@Repository
public interface PipRepository extends JpaRepository<Pip, Long> {

    List<Pip> findAllByEmployee(User employeeId);

    List<Pip> findAllByManager(User manager);

    
}
