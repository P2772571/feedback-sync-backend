package com.example.feedbacksync.service;

import com.example.feedbacksync.entity.Profile;
import com.example.feedbacksync.entity.User;
import com.example.feedbacksync.entity.enums.Role;
import com.example.feedbacksync.exceptions.UserAlreadyExistsException;
import com.example.feedbacksync.payloads.authentication.SignupRequest;
import com.example.feedbacksync.payloads.authentication.UsersWithProfileResponse;
import com.example.feedbacksync.payloads.profile.ProfileRequest;
import com.example.feedbacksync.payloads.profile.ProfileResponse;
import com.example.feedbacksync.repository.ProfileRepository;
import com.example.feedbacksync.repository.UserRespository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRespository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ProfileService profileService;
    private final ProfileRepository profileRepository;

    /**
     * Constructor for UserService class.
     * @param userRepository UserRespository object.
     * @param passwordEncoder PasswordEncoder object.
     */
    public UserService(UserRespository userRepository, PasswordEncoder passwordEncoder, ProfileService profileService, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.profileService = profileService;
        this.profileRepository = profileRepository;
    }

    /**
     * Create a new user account in the database
     * @param request - The request body containing the user details
     * @return - The created user entity
     */
    public User createUser(SignupRequest request) {
        // Check if user already exists by username
        if (userRepository.findByUsername(request.getUsername()) != null) {
            throw new UserAlreadyExistsException("Username is already taken.");
        }

        // Check if user already exists by email
        if (userRepository.findByEmail(request.getEmail()) != null) {
            throw new UserAlreadyExistsException("Email is already registered.");
        }

        // Create a new User entity
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));  // Hash the password
        newUser.setRole( request.getRole() !=  null ? Role.valueOf(request.getRole().toUpperCase()): Role.EMPLOYEE);  // Default role (You can adjust this based on your requirements)

        // Save user to the database
        return userRepository.save(newUser);
    }

    public UsersWithProfileResponse createNewUser(SignupRequest request) {
        // Check if user already exists by username
        if (userRepository.findByUsername(request.getUsername()) != null) {
            throw new UserAlreadyExistsException("Username is already taken.");
        }

        // Check if user already exists by email
        if (userRepository.findByEmail(request.getEmail()) != null) {
            throw new UserAlreadyExistsException("Email is already registered.");
        }

        // Create a new User entity
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));  // Hash the password
        newUser.setRole( request.getRole() !=  null ? Role.valueOf(request.getRole().toUpperCase()): Role.EMPLOYEE);  // Default role (You can adjust this based on your requirements)

        // Save user to the database
         newUser = userRepository.save(newUser);

         // Create Profile by Using Profile Requeset
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setFirstName("");
        profileRequest.setLastName("");
        profileRequest.setJobTitle("");
        profileRequest.setUsername(newUser.getUsername());
        profileRequest.setEmail(newUser.getEmail());
        ProfileResponse profileResponse = profileService.createUserProfile(newUser.getUsername(), profileRequest);

        return fromUserAndProfile(newUser, profileResponse);
    }

    /**
     * Find User by Username or Email
     * @param username - The username or email
     * @return - The user entity
     */
    public User findUserByUsernameOrEmail(String username) {
        return username.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$") ? userRepository.findByEmail(username) : userRepository.findByUsername(username);
    }

    /**
     * Change the password of the user
     * @param user - The user entity
     * @param password - The new password
     * @return - The updated user entity
     */
    public User changePassword (User user, String password){

        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    /**
     * Check if the old password is valid
     * @param user - The user entity
     * @param oldPassword - The old password
     * @return - True if the old password is valid, false otherwise
     */
    public boolean checkIfValidOldPassword(User user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    /**
     * Update User details
     * @param user - The user entity
     * @param username - The new username
     * @param email - The new email
     * @return - The updated user entity
     */
    public User updateUser(User user, String username, String email) {
        user.setEmail(email != null ? email : user.getEmail());
        user.setUsername(username != null ? username : user.getUsername());
        return userRepository.save(user);
    }

    /**
     * Find User by User Id
     * @param id - The user id
     * @return - The user entity
     */
    public User findUserByUserId(Long id){
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Delete User by User Id
     * @param id - The user id
     */
    public void deleteUserByUserId(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Find All Users
     * @return  The list of users with profile
     */
    public List<UsersWithProfileResponse> findAllUsers() {
        List<User> users = userRepository.findAll();
        return fromUsers(users);
    }

    /**
     * Find all users subordinates to a manager
     * @return The list of users with profile
     */
    public List<UsersWithProfileResponse> findSubordinates(Long id) {
        User manager = userRepository.findById(id).orElse(null);
        if (manager == null) {
            throw new RuntimeException("Manager not found with username: " + id);
        }
        List<Profile> profiles = profileRepository.findAllByManager(manager);
        return fromProfiles(profiles);
    }

    /**
     * Find all users with profile
     * @param profiles - The list of profiles
     * @return The list of users with profile
     */
    private List<UsersWithProfileResponse> fromProfiles(List<Profile> profiles) {
        return profiles.stream().map(profile -> {
            User user = profile.getUser();
            ProfileResponse profileResponse = new ProfileResponse();
            profileResponse.setProfileId(profile.getProfileId());
            profileResponse.setFirstName(profile.getFirstName());
            profileResponse.setLastName(profile.getLastName());
            profileResponse.setJobTitle(profile.getJobTitle());
            profileResponse.setEmail(user.getEmail());
            profileResponse.setUsername(user.getUsername());
            return fromUserAndProfile(user, profileResponse);

        }).toList();
    }

    /**
     * Find all the users and their profile for admin
     * @return The list of users with profile
     */

    public List<UsersWithProfileResponse> findAllUsersWithProfileForAdmin() {
        List<User> users = userRepository.findAll();
        return fromUsers(users);
    }


    /**
     * Convert List of User to List of UsersWithProfileResponse
     * @param users - The list of users
     * @return The list of users with profile
     */
    private   List<UsersWithProfileResponse> fromUsers(List<User> users) {
        return users.stream().map(user -> {
            ProfileResponse profile = profileService.getUserProfile(user.getUsername());
            return fromUserAndProfile(user, profile);

        }).toList();
    }

    private UsersWithProfileResponse fromUserAndProfile(User user, ProfileResponse profile) {
        UsersWithProfileResponse response = new UsersWithProfileResponse();
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setFullName(profile.getFullName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());
        response.setProfileId(profile.getProfileId());
        return response;
    }
}
