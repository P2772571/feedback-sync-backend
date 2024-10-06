package com.example.feedbacksync.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HealthCheckController class to handle health check requests
 */
@RestController
@RequestMapping("/public/api/health-check")
@CrossOrigin(origins = "http://localhost:5173")
public class HealthCheckController {

    @GetMapping
    public String healthCheck() {
        return "FeedBack Sync Service is up and running";
    }
}
