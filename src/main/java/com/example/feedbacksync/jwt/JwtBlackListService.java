package com.example.feedbacksync.jwt;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * JwtBlackListService class to handle blacklisting of JWT tokens
 */
@Service
public class JwtBlackListService {
    private final RedisTemplate<String, String> redisTemplate;
    private final String BLACKLIST_PREFIX = "blacklist:";

    /**
     * Constructor
     * @param redisTemplate - The redis template
     */
    public JwtBlackListService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Add token to blacklist with expiration time
     * @param token  - The token to blacklist
     * @param expirationTime - The expiration time of the token
     */
    public void blackListToken(String token, long expirationTime){
        redisTemplate.opsForValue().set(BLACKLIST_PREFIX + token, token, expirationTime, TimeUnit.MILLISECONDS);
    }


    /**
     * Check if token is blacklisted or not
     * @param token - The token to check
     * @return - True if token is blacklisted, false otherwise
     */
    public boolean isTokenBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + token));
    }

}
