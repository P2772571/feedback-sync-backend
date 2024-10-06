package com.example.feedbacksync.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * AuthEntryPointJwt class implements AuthenticationEntryPoint interface and overrides the commence method.
 * This class is used to handle authentication errors.
 */
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    /**
     * This method is used to handle authentication errors.
     * @param request HttpServletRequest object.
     * @param response HttpServletResponse object.
     * @param authException AuthenticationException object.
     * @throws IOException If an input or output exception occurs.
     * @throws ServletException If a servlet exception occurs.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // logger
        authException.printStackTrace();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", authException.getMessage());
        body.put("path", request.getServletPath());
    }
}
