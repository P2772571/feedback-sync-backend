package com.example.feedbacksync.repository;

import com.example.feedbacksync.entity.Profile;
import com.example.feedbacksync.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Profile findProfileByUser(User user);
    List<Profile> findAllByManager(User manager);  // Find all profiles of users managed by a manager
}
