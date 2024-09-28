package com.example.feedbacksync.payloads.profile;

import com.example.feedbacksync.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {
    private String firstName;
    private String lastName;
    private String jobTitle;
    private String email;
    private String username;
}
