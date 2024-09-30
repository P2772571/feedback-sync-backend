package com.example.feedbacksync.service;

import com.example.feedbacksync.entity.User;
import com.example.feedbacksync.entity.enums.Role;
import com.example.feedbacksync.exceptions.UserAlreadyExistsException;
import com.example.feedbacksync.payloads.authentication.SignupRequest;
import com.example.feedbacksync.repository.UserRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRespository userRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor for UserService class.
     * @param userRepository UserRespository object.
     * @param passwordEncoder PasswordEncoder object.
     */
    public UserService(UserRespository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
}
