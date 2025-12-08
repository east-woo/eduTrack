package com.eastwoo.toy.edutrack.auth.config;

import com.eastwoo.toy.edutrack.auth.entity.User;
import com.eastwoo.toy.edutrack.auth.enumtype.UserRole;
import com.eastwoo.toy.edutrack.auth.enumtype.UserStatus;
import com.eastwoo.toy.edutrack.auth.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminProperties adminProperties;

    @PostConstruct
    public void initAdmin() {
        if (!userRepository.existsByEmail(adminProperties.getEmail())) {
            User admin = User.builder()
                    .name("관리자")
                    .email(adminProperties.getEmail())
                    .password(passwordEncoder.encode(adminProperties.getPassword()))
                    .role(UserRole.ADMIN)
                    .status(UserStatus.ACTIVE)
                    .build();
            userRepository.save(admin);
        }
    }
}