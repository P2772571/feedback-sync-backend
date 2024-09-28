package com.example.feedbacksync.payloads.authentication;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    // id
    private Long id;
    private String username;
    private String email;
    private String accessToken;
    private String refreshToken;  // Add refresh token field
    private List<String> roles;

}
