package com.example.feedbacksync.payloads.authentication;

import com.example.feedbacksync.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UsersWithProfileResponse {
    private Long userId;
    private String username;
    private String fullName;
    private String email;
    private String role;
    private Long profileId;


}
