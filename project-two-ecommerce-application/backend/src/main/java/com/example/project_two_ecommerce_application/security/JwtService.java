package com.example.project_two_ecommerce_application.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

        private static final String SECRET_KEY = "1234567890123456789012345678901234567890123456789012345678901234";

        private Key getSignKey() {
            return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        }

        public String extractUsername(String token) {
            return extractClaim(token, Claims::getSubject);
        }

        public String extractRole(String token) {
            return extractAllClaims(token).get("role", String.class);
        }

        public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
            final Claims claims = extractAllClaims(token);
            return claimsResolver.apply(claims);
        }

        private Claims extractAllClaims(String token) {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }

        public String generateToken(String username, String role, Integer userId) {
            return Jwts.builder()
                    .setClaims(Map.of("role", role, "userId", userId))
                    .setSubject(username)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                    .signWith(getSignKey(), SignatureAlgorithm.HS256)
                    .compact();
        }

        public boolean isTokenValid(String token, String username) {
            return (username.equals(extractUsername(token)) && !isTokenExpired(token));
        }

        private boolean isTokenExpired(String token) {
            return extractExpiration(token).before(new Date());
        }

        private Date extractExpiration(String token) {
            return extractClaim(token, Claims::getExpiration);
        }
}
