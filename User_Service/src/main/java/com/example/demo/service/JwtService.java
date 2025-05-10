package com.example.demo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.demo.model.User;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {

	

    private final String SECRET_KEY = "MySuperSecretKeyForJwtAuthenticationMySuperSecretKeyForJwtAuthentication"; // Use a long key for security

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId()); // ✅ Add userId to token
        claims.put("role", user.getRole()); // ✅ Add role to token

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail()) // ✅ Set subject as 
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24-hour expiry
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    
//    public String generateToken(String username) {
//        Map<String, Object> claims = new HashMap<>();
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(username)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Token valid for 24 hours
//                .signWith(getKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
    
    
    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractUserId(String token) {
        try {
            return extractClaim(token, claims -> claims.get("id", String.class)); 
        } catch (Exception e) {
            // Handle the error (e.g., log the exception)
            throw new RuntimeException("Failed to extract user ID from token", e);
        }
    }

    
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String emailFromToken = extractUserName(token); // Extracts email
        final String emailFromUserDetails = userDetails.getUsername(); // Ensure it returns email
        return (emailFromToken.equalsIgnoreCase(emailFromUserDetails) && !isTokenExpired(token));
    }


    public boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false; 
        }
    }
    
    private boolean isTokenExpired(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }
}
