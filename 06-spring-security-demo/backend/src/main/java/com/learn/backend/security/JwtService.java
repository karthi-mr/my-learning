package com.learn.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final SecurityConfigProperties properties;

    private SecretKey getSigninKey() {
        return Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(UserDetails userDetails) {
        return this.generateToken(userDetails, this.properties.getRefreshExpiration(), "access");
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return this.generateToken(userDetails, this.properties.getRefreshExpiration(), "refresh");
    }

    private String generateToken(UserDetails userDetails, long expiration, String tokenType) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claims(Map.of("type", tokenType))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigninKey())
                .compact();
    }

    public String extractUsername(String token) {
        return this.extractClaims(token, Claims::getSubject);
    }

    public String extractTokenType(String token) {
        Claims claims = this.extractAllClaims(token);
        return claims.get("type", String.class);
    }

    public <T> T extractClaims(String token, Function<Claims, T> resolver) {
        Claims claims = this.extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(this.getSigninKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token, UserDetails userDetails, String expectedType) {
        String username = this.extractUsername(token);
        String tokenType = this.extractTokenType(token);

        return username.equals(userDetails.getUsername()) &&
                tokenType.equals(expectedType) &&
                !this.isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expiration = this.extractClaims(token, Claims::getExpiration);
        return expiration.before(new Date());
    }
}
