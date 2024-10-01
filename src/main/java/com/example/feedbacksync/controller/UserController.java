package com.example.feedbacksync.controller;

import com.example.feedbacksync.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    /**
     * Constructor for UserController class.
     * @param userService UserService object.
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }


    /**
     * Get all users
     * @return - The list of all users with their profiles
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        try{
            return ResponseEntity.ok(userService.findAllUsers());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error occurred while fetching users");
        }

    }

    /**
     * Get all users with their profiles
     * @return - The list of all users with their profiles
     */
    @GetMapping("/subordinates/{managerId}")
    public ResponseEntity<?> getSubordinates(@PathVariable Long managerId) {
        try{
            return ResponseEntity.ok(userService.findSubordinates(managerId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error occurred while fetching subordinates");
        }

    }
}
