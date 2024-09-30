package com.example.feedbacksync.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * AuthTokenFilter class extends OncePerRequestFilter class.
 * This class is used to filter the request and response.
 */
@Component
@Setter
@Getter
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * This method is used to filter the request and response.
     * @param request HttpServletRequest object.
     * @param response HttpServletResponse object.
     * @param filterChain FilterChain object.
     * @throws ServletException If a servlet exception occurs.
     * @throws IOException If an input or output exception occurs.
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        try{
            String jwt = parseJwt(request);
            System.out.println("Validating Token: " +jwtUtils.validateJwtToken(jwt));
            if(jwt !=  null && jwtUtils.validateJwtToken(jwt)){
                String username  = jwtUtils.getUsernameFromJwt(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }catch (Exception e){
            System.out.println("Cannot set user authentication: " + e.getMessage());
        }

        filterChain.doFilter(request,response);

    }

    /**
     * Parse JWT token from the request
     * @param request - The request
     * @return - The JWT token
     */
    private String parseJwt(HttpServletRequest request){
        String jwt =  jwtUtils.getJwtFromHeader(request);
        return jwt;
    }
}
