package com.example.feedbacksync.controller;

import com.example.feedbacksync.entity.User;
import com.example.feedbacksync.jwt.JwtBlackListService;
import com.example.feedbacksync.jwt.JwtUtils;
import com.example.feedbacksync.payloads.authentication.LoginRequest;
import com.example.feedbacksync.payloads.authentication.LoginResponse;
import com.example.feedbacksync.payloads.authentication.SignupRequest;
import com.example.feedbacksync.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/public/api/auth")
public class AuthController {

    private final JwtUtils jwtUtils;

    private final AuthenticationManager authenticationManager;

    private final UserService userService;
    private final JwtBlackListService jwtBlackListService;

    public AuthController(JwtUtils jwtUtils, AuthenticationManager authenticationManager, UserService userService, JwtBlackListService jwtBlackListService, JwtBlackListService jwtBlackListService1) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtBlackListService = jwtBlackListService1;
    }

    /**
     * Login endpoint to authenticate the user
     * @param loginRequest - The request body containing the user credentials
     * @return - The response entity containing the user details, access token, and refresh token
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try{
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        }catch (AuthenticationException exception){
            Map<String, Object> body = new HashMap<>();
            body.put("message", "Bad Credentials");
            body.put("status", HttpStatus.NOT_FOUND);

            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = userService.findUserByUsernameOrEmail(userDetails.getUsername());

        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);
        String refreshToken = jwtUtils.generateRefreshToken(userDetails);
        List<String> role = new ArrayList<>();
        for (GrantedAuthority grantedAuthority : userDetails.getAuthorities()) {
            String authority = grantedAuthority.getAuthority();
            role.add(authority);
        }

        LoginResponse response = new LoginResponse(user.getId(), userDetails.getUsername(), jwtToken,refreshToken, role);
        return ResponseEntity.ok(response);

    }



    /**
     *  Register a new user with the system
     * @param registrationRequest - The request body containing the user details
     * @return - The response entity containing the user details, access token, and refresh token
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody SignupRequest registrationRequest) {
        User user;
        try{
            user = userService.createUser(registrationRequest);
        } catch (Exception e ){
            Map<String, String> response = new HashMap<>();
            response.put("status", "fail");
            response.put("message", e.getMessage());

            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }



        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(registrationRequest.getUsername(), registrationRequest.getPassword())
        );


        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();


        String accessToken = jwtUtils.generateTokenFromUsername(userDetails);
        String refreshToken = jwtUtils.generateRefreshToken(userDetails);

        List<String> roles = new ArrayList<>();
        for (GrantedAuthority grantedAuthority : userDetails.getAuthorities()) {
            String authority = grantedAuthority.getAuthority();
            roles.add(authority);
        }


        LoginResponse response = new LoginResponse( user.getId(), userDetails.getUsername(), accessToken, refreshToken, roles);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Forgot password endpoint
     * @return - The response entity containing the success message
     */
    @GetMapping("/forgot-password")
    public String forgotPassword(){
        return "Successfully Update the password";
    }

    /**
     * Logout endpoint
     * @return - The response entity containing the success message
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request){

        String token = jwtUtils.getJwtFromHeader(request);

        if (token == null){
            Map<String, String> response = new HashMap<>();
            response.put("status", "fail");
            response.put("message", "Token is missing or invalid.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        }

        if (!jwtUtils.validateJwtToken(token)) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "fail");
            response.put("message", "Token is invalid or expired.");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        SecurityContextHolder.clearContext();
        long expirationTime = jwtUtils.getExpirationTimeFromJwt(token);
//        jwtBlackListService.blackListToken(token, expirationTime - System.currentTimeMillis());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Logout successful");
        return new ResponseEntity<>(response, HttpStatus.OK);


    }
}
