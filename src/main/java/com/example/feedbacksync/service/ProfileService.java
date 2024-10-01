package com.example.feedbacksync.service;

import com.example.feedbacksync.entity.Profile;
import com.example.feedbacksync.entity.User;
import com.example.feedbacksync.payloads.profile.ProfileRequest;
import com.example.feedbacksync.payloads.profile.ProfileResponse;
import com.example.feedbacksync.repository.ProfileRepository;
import com.example.feedbacksync.repository.UserRespository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * ProfileService class to handle profile related operations
 * This class is used to get and update user profiles
 */
@Service
public class ProfileService {
    private final ProfileRepository profileRepository;

    private final UserRespository userRespository;

    /**
     * Constructor for ProfileService class.
     * @param profileRepository ProfileRepository object.
     * @param userRespository UserRespository object.
     */
    public ProfileService(ProfileRepository profileRepository, UserRespository userRespository) {
        this.profileRepository = profileRepository;
        this.userRespository = userRespository;
    }

    /**
     * Get User Profile
     * @param username - The username or email
     * @return - The profile response
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
     * @param username - The username or email
     * @param profileRequest - The profile request
     * @return - The created profile response
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

    /**
     * Update User Profile
     * @param username - The username or email
     * @param profileRequest - The profile request
     * @return - The updated profile response
     *
     */
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

    /**
     * Delete User Profile
     * @param username - The username or email
     */
    public void deleteProfile(String username) {
        User user = username.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$") ? userRespository.findByEmail(username) : userRespository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found with username or email: " + username);
        }

        Profile profile = profileRepository.findProfileByUser(user);
        if (profile == null) {
            throw new IllegalArgumentException("Profile not found for user: " + username);
        }

        profileRepository.delete(profile);
    }

    /**
     * Get All profiles manager by a manager
     * @return - The list of profile responses
     */
    public List<ProfileResponse> getManagedProfiles(User manager) {

        List<Profile> profiles = profileRepository.findAllByManager(manager);
        List<ProfileResponse> responses = new ArrayList<>();
        for (Profile profile : profiles) {
            ProfileResponse response = new ProfileResponse();
            response.setProfileId(profile.getProfileId());
            response.setFirstName(profile.getFirstName());
            response.setLastName(profile.getLastName());
            response.setJobTitle(profile.getJobTitle());
            response.setEmail(profile.getUser().getEmail());
            response.setUsername(profile.getUser().getUsername());
            responses.add(response);
        }

        return responses;
    }
}
