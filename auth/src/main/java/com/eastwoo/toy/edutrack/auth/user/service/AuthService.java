package com.eastwoo.toy.edutrack.auth.user.service;

import com.eastwoo.toy.edutrack.auth.user.dto.AuthResponse;
import com.eastwoo.toy.edutrack.auth.user.entity.User;
import com.eastwoo.toy.edutrack.auth.global.exception.BusinessException;
import com.eastwoo.toy.edutrack.auth.global.exception.ErrorCode;
import com.eastwoo.toy.edutrack.auth.user.repository.UserRepository;
import com.eastwoo.toy.edutrack.auth.global.security.JwtService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
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

    public AuthResponse login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }

        String accessToken = jwtService.generateAccessToken(user.getId(), user.getRole());
        String refreshToken = jwtService.generateRefreshToken(user.getId());

        redisTemplate.opsForValue()
                .set("refreshToken:" + user.getId(), refreshToken, 14, TimeUnit.DAYS);

        return new AuthResponse(accessToken, refreshToken);
    }

    public String refreshAccessToken(String refreshToken) {
        Claims claims = jwtService.parseToken(refreshToken);
        Long userId = Long.valueOf(claims.getSubject());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return jwtService.generateAccessToken(user.getId(), user.getRole());
    }
}