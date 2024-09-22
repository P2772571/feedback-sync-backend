package com.example.feedbacksync.service;

import com.example.feedbacksync.entity.User;
import com.example.feedbacksync.repository.UserRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRespository userRespository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = username.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")? userRespository.findByEmail(username) : userRespository.findByUsername(username);
        if (user != null){
            return org.springframework.security.core.userdetails.User
                    .builder()
                    .username(username.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$") ? user.getEmail() : user.getUsername())
                    .password(user.getPassword())
                    .roles(String.valueOf(user.getRole()))
                    .authorities(new SimpleGrantedAuthority(user.getRole().name()))
                    .build();
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }


    }
}
