package com.learn.backend.user;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findUserByUsername("john").isEmpty()) {
            userRepository.save(
                    AppUser.builder()
                            .username("john")
                            .password(this.passwordEncoder.encode("admin"))
                            .role("ROLE_USER")
                            .build()
            );
        }
        if (userRepository.findUserByUsername("admin").isEmpty()) {
            userRepository.save(
                    AppUser.builder()
                            .username("admin")
                            .password(this.passwordEncoder.encode("admin"))
                            .role("ROLE_ADMIN")
                            .build()
            );
        }
    }
}
