package com.example.feedbacksync.seeders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SeederRunner implements CommandLineRunner {

    @Autowired
    private UserSeeder userSeeder;


    @Override
    public void run(String... args) throws Exception {
        // Seed data in the correct order
        userSeeder.seed();

    }
}
