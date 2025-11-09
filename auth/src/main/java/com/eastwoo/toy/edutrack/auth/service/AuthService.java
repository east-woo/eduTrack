package com.eastwoo.toy.edutrack.auth.service;

import com.eastwoo.toy.edutrack.auth.entity.User;
import com.eastwoo.toy.edutrack.auth.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public String login(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("비밀번호가 올바르지 않습니다.");
        }

        return jwtService.generateAccessToken(user.getId(), user.getRole());
    }

    public User register(String name, String email, String password, String role) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        User user = User.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(role)
                .build();

        return userRepository.save(user);
    }

    /**
     * Refreshes the access token using the provided refresh token.
     *
     * @param refreshToken JWT refresh token
     * @return A new access token
     * @throws RuntimeException if the refresh token is invalid or expired
     */
    public String refreshAccessToken(String refreshToken) {
        try {
            // 1. 리프레시 토큰 파싱 및 유효성 검사
            Claims claims = jwtService.parseToken(refreshToken);
            Long userId = Long.valueOf(claims.getSubject());

            // 2. 사용자 확인
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

            // 3. 새로운 액세스 토큰 생성
            return jwtService.generateAccessToken(user.getId(), user.getRole());
        } catch (JwtException | IllegalArgumentException ex) {
            // 리프레시 토큰이 유효하지 않음
            throw new RuntimeException("토큰이 유효하지 않거나 만료되었습니다.");
        }
    }
}