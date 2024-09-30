package com.example.feedbacksync.seeders;


import com.example.feedbacksync.entity.Profile;
import com.example.feedbacksync.entity.User;

import com.example.feedbacksync.entity.enums.Role;
import com.example.feedbacksync.repository.ProfileRepository;
import com.example.feedbacksync.repository.UserRespository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserSeeder {

    private final UserRespository userRepository;

    private final ProfileRepository profileRepository;

    private final PasswordEncoder passwordEncoder;

    public UserSeeder(UserRespository userRepository, ProfileRepository profileRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void seed() {
        if (userRepository.count() == 0) {
            User rizwan = new User();
            rizwan.setUsername("rizwan");
            rizwan.setEmail("rizwan@example.com");
            rizwan.setPassword(passwordEncoder.encode("password123"));
            rizwan.setRole(Role.EMPLOYEE);

            User mohammad = new User();
            mohammad.setUsername("mohammad");
            mohammad.setEmail("mohammad@example.com");
            mohammad.setPassword(passwordEncoder.encode("password123"));
            mohammad.setRole(Role.EMPLOYEE);

            User saad = new User();
            saad.setUsername("saad");
            saad.setEmail("saad@example.com");
            saad.setPassword(passwordEncoder.encode("password123"));
            saad.setRole(Role.MANAGER);

            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole(Role.ADMIN);

            User john = new User();
            john.setUsername("john");
            john.setEmail("john@example.com");
            john.setPassword(passwordEncoder.encode("password123"));
            john.setRole(Role.EMPLOYEE);

            User jane = new User();
            jane.setUsername("jane");
            jane.setEmail("jane@example.com");
            jane.setPassword(passwordEncoder.encode("password123"));
            jane.setRole(Role.EMPLOYEE);

            User jacob = new User();
            jacob.setUsername("jacob");
            jacob.setEmail("jacob@example.com");
            jacob.setPassword(passwordEncoder.encode("password123"));
            jacob.setRole(Role.EMPLOYEE);

            User jessica = new User();
            jessica.setUsername("jessica");
            jessica.setEmail("jessica@example.com");
            jessica.setPassword(passwordEncoder.encode("password123"));
            jessica.setRole(Role.MANAGER);

            User jennifer = new User();
            jennifer.setUsername("jennifer");
            jennifer.setEmail("jennifer@example.com");
            jennifer.setPassword(passwordEncoder.encode("password123"));
            jennifer.setRole(Role.MANAGER);

            User jason = new User();
            jason.setUsername("jason");
            jason.setEmail("jason@example.com");
            jason.setPassword(passwordEncoder.encode("password123"));
            jason.setRole(Role.EMPLOYEE);

            User jared = new User();
            jared.setUsername("jared");
            jared.setEmail("jared@example.com");
            jared.setPassword(passwordEncoder.encode("password123"));
            jared.setRole(Role.EMPLOYEE);

            User ali = new User();
            ali.setUsername("ali");
            ali.setEmail("ali@example.com");
            ali.setPassword(passwordEncoder.encode("password123"));
            ali.setRole(Role.EMPLOYEE);
            


            userRepository.saveAll(
                List.of(
                    rizwan, mohammad, saad, admin, john, jane, jacob, jessica, jennifer, jason, jared, ali
                )
            );

            // Create Profiles
            Profile rizwanProfile = new Profile();
            rizwanProfile.setFirstName("Rizwan");
            rizwanProfile.setLastName("Lunat");
            rizwanProfile.setJobTitle("Frontend Developer");
            rizwanProfile.setUser(rizwan);
            rizwanProfile.setManager(saad);

            Profile mohammadProfile = new Profile();
            mohammadProfile.setFirstName("Mohammad");
            mohammadProfile.setLastName("Lorgat");
            mohammadProfile.setJobTitle("Backend Developer");
            mohammadProfile.setUser(mohammad);
            mohammadProfile.setManager(saad);

            Profile saadProfile = new Profile();
            saadProfile.setFirstName("Saad");
            saadProfile.setLastName("Patel");
            saadProfile.setJobTitle("Project Manager");
            saadProfile.setUser(saad);

            Profile adminProfile = new Profile();
            adminProfile.setFirstName("Admin");
            adminProfile.setLastName("Admin");
            adminProfile.setJobTitle("Admin");
            adminProfile.setUser(admin);

            Profile johnProfile = new Profile();
            johnProfile.setFirstName("John");
            johnProfile.setLastName("Doe");
            johnProfile.setJobTitle("Digital Marketing Specialist");
            johnProfile.setUser(john);
            johnProfile.setManager(jessica);

            Profile janeProfile = new Profile();
            janeProfile.setFirstName("Jane");
            janeProfile.setLastName("Doe");
            janeProfile.setJobTitle("Content Writer");
            janeProfile.setUser(jane);
            janeProfile.setManager(jessica);

            Profile jacobProfile = new Profile();
            jacobProfile.setFirstName("Jacob");
            jacobProfile.setLastName("Doe");
            jacobProfile.setJobTitle("SEO Specialist");
            jacobProfile.setUser(jacob);
            jacobProfile.setManager(jessica);

            Profile jessicaProfile = new Profile();
            jessicaProfile.setFirstName("Jessica");
            jessicaProfile.setLastName("Smith");
            jessicaProfile.setJobTitle("Marketing Manager");
            jessicaProfile.setUser(jessica);

            Profile jenniferProfile = new Profile();
            jenniferProfile.setFirstName("Jennifer");
            jenniferProfile.setLastName("Smith");
            jenniferProfile.setJobTitle("Finance Manager");
            jenniferProfile.setUser(jennifer);

            Profile jasonProfile = new Profile();
            jasonProfile.setFirstName("Jason");
            jasonProfile.setLastName("Doe");
            jasonProfile.setJobTitle("Financial Analyst");
            jasonProfile.setUser(jason);
            jasonProfile.setManager(jennifer);

            Profile jaredProfile = new Profile();
            jaredProfile.setFirstName("Jared");
            jaredProfile.setLastName("Doe");
            jaredProfile.setJobTitle("Junior Accountant");
            jaredProfile.setUser(jared);
            jaredProfile.setManager(jennifer);

            Profile aliProfile = new Profile();
            aliProfile.setFirstName("Ali");
            aliProfile.setLastName("Doe");
            aliProfile.setJobTitle("Senior Accountant");
            aliProfile.setUser(ali);
            aliProfile.setManager(jennifer);

            // First Save Admin Profile then Manager then Employee
            profileRepository.saveAll(
                List.of(
                    adminProfile, saadProfile, 
                    jessicaProfile, 
                    jenniferProfile 
                    ,rizwanProfile, 
                    mohammadProfile, 
                    johnProfile, 
                    janeProfile, 
                    jacobProfile, 
                    jasonProfile, 
                    jaredProfile, 
                    aliProfile
                )
            );

        }
    }
}

