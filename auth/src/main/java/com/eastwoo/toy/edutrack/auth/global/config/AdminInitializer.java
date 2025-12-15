package com.eastwoo.toy.edutrack.auth.global.config;

import com.eastwoo.toy.edutrack.auth.user.entity.User;
import com.eastwoo.toy.edutrack.auth.user.enumtype.UserRole;
import com.eastwoo.toy.edutrack.auth.user.enumtype.UserStatus;
import com.eastwoo.toy.edutrack.auth.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminProperties adminProperties;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {

        // 1. 기존 관리자 전부 삭제
        int deleted = userRepository.deleteByRole(UserRole.ADMIN);
        log.info("[ADMIN-INIT] deleted admin count={}", deleted);

        // 2. 관리자 재생성
        User admin = User.builder()
                .name("관리자")
                .email(adminProperties.getEmail())
                .password(passwordEncoder.encode(adminProperties.getPassword()))
                .role(UserRole.ADMIN)
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(admin);

        log.info("[ADMIN-INIT] admin recreated email={}", admin.getEmail());
    }
}