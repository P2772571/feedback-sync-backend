package com.example.feedbacksync.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;

import java.util.Date;

@Component
@Setter
@Getter
public class JwtUtils {

    // Logger
    @Value("${spring.app.jwt-secret}")
    private String jwtSecret;

    @Value("${spring.app.jwt-expiration-ms}")
    private  long jwtExpirationMs;

    @Value("${spring.app.jwt-refresh-expiration-ms}")
    private long jwtRefreshExpirationMs;


    /**
     * Get JWT from Header of the request
     * @param request - The request
     * @return - The JWT token
     */
    public String getJwtFromHeader(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return  null;
    }

    /**
     * Generate JWT Token from Username
     * @param userDetails - The user details
     * @return - The JWT token
     */
    public String  generateTokenFromUsername(UserDetails userDetails){
        String username = userDetails.getUsername();
        return Jwts
                .builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date((new Date().getTime()+jwtExpirationMs)))
                .signWith(key())
                .compact();
    }

    public String getUsernameFromJwt(String token){
        return  Jwts
                .parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // Generate Refresh Token (Longer expiration)
    @SuppressWarnings("deprecation")
    public String generateRefreshToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtRefreshExpirationMs))
                .signWith(key())
                .compact();
    }

    /**
     * Get expiration time from JWT
     * @return - The expiration time
     */
    public long getExpirationTimeFromJwt(String token) {
        return Jwts
                .parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload().getExpiration().getTime();
    }

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateJwtToken(String authToken){
        try{
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        }catch (SecurityException | MalformedJwtException e){
            System.out.println("Invalid Jwt Token: "+ e.getMessage());
        }catch (ExpiredJwtException e){
            System.out.println("Jwt Toke is Expired: " + e.getMessage());
        }catch (UnsupportedJwtException e){
           System.out.println("Jwt Token is Unsupported: " + e.getMessage());
        }catch (IllegalArgumentException e){
            System.out.println("Jwt Claims String is empty: "+ e.getMessage());
        }
        return  false;
    }
}
