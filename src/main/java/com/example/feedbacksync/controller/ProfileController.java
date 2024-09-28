package com.example.feedbacksync.controller;

import com.example.feedbacksync.entity.User;
import com.example.feedbacksync.jwt.JwtUtils;
import com.example.feedbacksync.payloads.authentication.LoginResponse;
import com.example.feedbacksync.payloads.profile.ProfileRequest;
import com.example.feedbacksync.payloads.profile.ProfileResponse;
import com.example.feedbacksync.service.ProfileService;
import com.example.feedbacksync.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;


    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping
    public ResponseEntity<?> getUserProfile(HttpServletRequest request){


        try{
            // Getting username from Security Context
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            ProfileResponse response = profileService.getUserProfile(username);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Create a new user profile
     * @param profileRequest
     * @return
     */
    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping
    public  ResponseEntity<?> createUserProfile(@RequestBody ProfileRequest profileRequest){
        try{


            // Getting username from Security Context
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            ProfileResponse response = profileService.createUserProfile(username, profileRequest);


            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update user profile
     * @param profileRequest
     * @return
     */
    @CrossOrigin(origins = "http://localhost:5173")
    @PutMapping()
    public  ResponseEntity<?> updateUserProfile(@RequestBody ProfileRequest profileRequest) {
        try{
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            ProfileResponse response = profileService.updateProfile(username, profileRequest);
             User user = userService.findUserByUsernameOrEmail(username);
            LoginResponse loginResponse = null;

            // If the username or email has been updated, re-authenticate the user
            if (
                   (profileRequest.getUsername() != null && !profileRequest.getUsername().equals(username))
                || (profileRequest.getEmail() != null && !profileRequest.getEmail().equals(user.getEmail()))
            ){

                // Update the User
                User updatedUser = userService.updateUser( user, profileRequest.getUsername(), profileRequest.getEmail());
                UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                String accessToken = jwtUtils.generateTokenFromUsername(updatedUser.getUsername());


                // Generate Access Token and RefreshToken based on Username
                loginResponse = new LoginResponse();
                loginResponse.setId(user.getId());
                loginResponse.setEmail(updatedUser.getEmail());
                loginResponse.setUsername(updatedUser.getUsername());
                loginResponse.setAccessToken(accessToken);
                loginResponse.setRefreshToken(jwtUtils.generateRefreshToken(updatedUser.getUsername()));

                 List<String> role = new ArrayList<>();
                 for (GrantedAuthority grantedAuthority : userDetails.getAuthorities()) {
                        String authority = grantedAuthority.getAuthority();
                        role.add(authority);
                }
                 loginResponse.setRoles(role);

                 response.setEmail(updatedUser.getEmail());
                 response.setUsername(updatedUser.getUsername());

            }else {
                response.setUsername(user.getUsername());
                response.setEmail(user.getEmail());
            }

            Map<String , Object> body = new HashMap<>();
            body.put("profile", response);
            body.put("user", loginResponse);

            return new ResponseEntity<>(body, HttpStatus.OK);
        }
        catch (Exception e ){
             return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
