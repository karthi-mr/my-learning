package com.learn.backend.auth;

import com.learn.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    public AuthResponse login(LoginRequest request) {
        this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        var userDetails = this.userDetailsService.loadUserByUsername(request.username());

        String accessToken = this.jwtService.generateAccessToken(userDetails);
        String refreshToken = this.jwtService.generateRefreshToken(userDetails);

        return new AuthResponse(accessToken, refreshToken, "Bearer");
    }

    public AuthResponse refresh(TokenRefreshRequest request) {
        final String refreshToken = request.refreshToken();
        final String username = this.jwtService.extractUsername(refreshToken);

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

        if (!jwtService.isTokenValid(refreshToken, userDetails, "refresh")) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        final String newAccessToken = this.jwtService.generateAccessToken(userDetails);

        return new AuthResponse(newAccessToken, refreshToken, "Bearer");
    }
}
