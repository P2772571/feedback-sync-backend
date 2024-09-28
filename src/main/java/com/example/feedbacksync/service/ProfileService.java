package com.example.feedbacksync.service;

import com.example.feedbacksync.entity.Profile;
import com.example.feedbacksync.entity.User;
import com.example.feedbacksync.payloads.profile.ProfileRequest;
import com.example.feedbacksync.payloads.profile.ProfileResponse;
import com.example.feedbacksync.repository.ProfileRepository;
import com.example.feedbacksync.repository.UserRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRespository userRespository;

    /**
     * Get the profile of a user
     */
    public ProfileResponse getUserProfile(String username) {
        User user = username.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")? userRespository.findByEmail(username) : userRespository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found with username or email: " + username);
        }


        Profile profile = profileRepository.findProfileByUser(user);

        if (profile == null) {
            throw new RuntimeException("Profile not found for user: " + username);
        }
        ProfileResponse response = new ProfileResponse();
        response.setProfileId(profile.getProfileId());
        response.setFirstName(profile.getFirstName());
        response.setLastName(profile.getLastName());
        response.setJobTitle(profile.getJobTitle());
        response.setEmail(user.getEmail());
        response.setUsername(user.getUsername());

        // Tailor the response based on the user's role
        switch (user.getRole()) {
            case EMPLOYEE:
               User manager = profile.getManager();
               if (manager != null){
                   Profile managerProfile = profileRepository.findProfileByUser(manager);
                   ProfileResponse managerResponse = new ProfileResponse();
                        managerResponse.setProfileId(managerProfile.getProfileId());
                        managerResponse.setFirstName(managerProfile.getFirstName());
                        managerResponse.setLastName(managerProfile.getLastName());
                        managerResponse.setJobTitle(managerProfile.getJobTitle());
                        managerResponse.setEmail(manager.getEmail());
                        managerResponse.setUsername(manager.getUsername());
                        response.setManager(managerResponse);

               }
                break;

            case MANAGER:
                List<Profile> managedEmployees = profileRepository.findAllByManager(user);
                List<ProfileResponse> employeeResponses = new ArrayList<>();
                for (Profile employee : managedEmployees) {
                    ProfileResponse employeeResponse = new ProfileResponse();
                    employeeResponse.setProfileId(employee.getProfileId());
                    employeeResponse.setFirstName(employee.getFirstName());
                    employeeResponse.setLastName(employee.getLastName());
                    employeeResponse.setJobTitle(employee.getJobTitle());
                    employeeResponse.setEmail(employee.getUser().getEmail());
                    employeeResponse.setUsername(employee.getUser().getUsername());
                    employeeResponses.add(employeeResponse);
                }
                response.setEmployees(employeeResponses);
                break;

            case ADMIN:
                // No additional data for admins, return only basic info
                break;
        }

        return response;
    }


    /**
     * Create User Profile
     */
    public ProfileResponse createUserProfile(String username, ProfileRequest profileRequest) {
        User user = username.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$") ? userRespository.findByEmail(username) : userRespository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found with username or email: " + username);
        }

        Profile profile = new Profile();
        profile.setUser(user);
        profile.setFirstName(profileRequest.getFirstName());
        profile.setLastName(profileRequest.getLastName());
        profile.setJobTitle(profileRequest.getJobTitle());


        Profile  savedProfile =profileRepository.save(profile);

        ProfileResponse response = new ProfileResponse();
        response.setProfileId(savedProfile.getProfileId());
        response.setFirstName(savedProfile.getFirstName());
        response.setLastName(savedProfile.getLastName());
        response.setJobTitle(savedProfile.getJobTitle());
        response.setEmail(user.getEmail());
        response.setUsername(user.getUsername());

        return response;
    }

    public ProfileResponse updateProfile(String username, ProfileRequest profileRequest) {
        User user = username.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$") ? userRespository.findByEmail(username) : userRespository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found with username or email: " + username);
        }

        Profile profile = profileRepository.findProfileByUser(user);
        if (profile == null) {
            throw new RuntimeException("Profile not found for user: " + username);
        }


        profile.setFirstName(profileRequest.getFirstName() != null? profileRequest.getFirstName() : profile.getFirstName());
        profile.setLastName(profileRequest.getLastName() != null? profileRequest.getLastName() : profile.getLastName());
        profile.setJobTitle(profileRequest.getJobTitle() != null? profileRequest.getJobTitle() : profile.getJobTitle());

        Profile savedProfile = profileRepository.save(profile);
        ProfileResponse response = new ProfileResponse();
        response.setProfileId(savedProfile.getProfileId());
        response.setFirstName(savedProfile.getFirstName());
        response.setLastName(savedProfile.getLastName());
        response.setJobTitle(savedProfile.getJobTitle());

        return response;
    }
}
