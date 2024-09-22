package com.example.feedbacksync.repository;

import com.example.feedbacksync.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface UserRespository  extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String username);
}
