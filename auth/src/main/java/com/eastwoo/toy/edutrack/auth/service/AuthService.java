package com.eastwoo.toy.edutrack.auth.service;

import com.eastwoo.toy.edutrack.auth.dto.AuthResponse;
import com.eastwoo.toy.edutrack.auth.entity.User;
import com.eastwoo.toy.edutrack.auth.enumtype.UserRole;
import com.eastwoo.toy.edutrack.auth.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;

    /* 로그인 메서드 */
    public AuthResponse login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 올바르지 않습니다.");
        }

        String accessToken = jwtService.generateAccessToken(user.getId(), user.getRole());
        String refreshToken = jwtService.generateRefreshToken(user.getId());

        redisTemplate.opsForValue()
                .set("refreshToken:" + user.getId(), refreshToken, 14, TimeUnit.DAYS);

        return new AuthResponse(accessToken, refreshToken);
    }

    /*회원가입*/
    public User register(String name, String email, String password, UserRole role) {
        // 이메일 중복 체크: 이미 해당 이메일로 등록된 사용자가 있는지 확인
        if (userRepository.findByEmail(email).isPresent()) {
            // 이메일 중복 시 예외를 발생시킴
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

    /*AccessToken 재발행*/
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