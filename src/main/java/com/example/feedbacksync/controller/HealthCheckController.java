package com.example.feedbacksync.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/api/health-check")
public class HealthCheckController {

    @GetMapping
    public String healthCheck() {
        return "FeedBack Sync Service is up and running";
    }
}
