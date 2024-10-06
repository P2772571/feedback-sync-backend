package com.example.feedbacksync.seeders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * SeederRunner class is used to run all the seeders in the correct order
 * when the application starts. This class implements CommandLineRunner interface and overrides the run method.
 */
@Component
public class SeederRunner implements CommandLineRunner {

    @Autowired
    private UserSeeder userSeeder;


    /**
     * This method is used to run all the seeders in the correct order when the application starts.
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        // Seed data in the correct order
        userSeeder.seed();

    }
}
