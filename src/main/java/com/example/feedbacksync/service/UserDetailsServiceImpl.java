package com.example.feedbacksync.service;

import com.example.feedbacksync.entity.User;
import com.example.feedbacksync.repository.UserRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * UserDetailsServiceImpl class implements UserDetailsService interface and overrides the loadUserByUsername method.
 * This class is used to load user-specific data. It is used by the DaoAuthenticationProvider to authenticate a user.
 * The loadUserByUsername method returns a UserDetails object that contains the user's information.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRespository userRespository;

    /**
     * Constructor for UserDetailsServiceImpl class.
     * @param userRespository UserRespository object.
     */
    public UserDetailsServiceImpl(UserRespository userRespository) {
        this.userRespository = userRespository;
    }

    /**
     * This method is used to load user-specific data.
     * @param username Username of the user.
     * @return UserDetails object that contains the user's information.
     * @throws UsernameNotFoundException If the user is not found.
     */
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
