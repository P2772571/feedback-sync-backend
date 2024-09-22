package com.example.feedbacksync.entity;

import com.example.feedbacksync.entity.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 30)
    @Column(unique = true)
    private String username;

    @NotBlank
    @Size(max = 100)
    @Column(unique = true)
    private String email;

    @NotBlank
    @Size(max = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;



}