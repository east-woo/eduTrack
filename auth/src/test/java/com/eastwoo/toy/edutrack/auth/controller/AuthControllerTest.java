package com.eastwoo.toy.edutrack.auth.controller;


import com.eastwoo.toy.edutrack.auth.dto.AuthResponse;
import com.eastwoo.toy.edutrack.auth.entity.User;
import com.eastwoo.toy.edutrack.auth.repository.UserRepository;
import com.eastwoo.toy.edutrack.auth.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private AuthService authService;

    @BeforeEach
    void 초기화() {
        // DB 초기화
        userRepository.deleteAll();
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    void 회원가입_성공() throws Exception {
        // Given: 회원가입 요청 데이터
        String name = "홍길동";
        String email = "hong@company.com";
        String password = "password123";
        String role = "STUDENT";

        // When: 회원가입 API 호출
        mockMvc.perform(post("/api/auth/register")
                        .param("name", name)
                        .param("email", email)
                        .param("password", password)
                        .param("role", role)
                        .with(csrf()))
                // Then: 상태 200 OK, 응답 JSON 검증
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    void 로그인_성공_및_리프레시토큰_저장() throws Exception {
        // Given: 회원가입된 사용자
        User user = authService.register("홍길동", "hong@company.com", "password123", "STUDENT");

        // When: 로그인 요청
        String response = mockMvc.perform(post("/api/auth/login")
                        .param("email", "hong@company.com")
                        .param("password", "password123")
                        .with(csrf()))
                // Then: 상태 200 OK, accessToken과 refreshToken 존재
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", notNullValue()))
                .andExpect(jsonPath("$.refreshToken", notNullValue()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Then: Redis에 refreshToken 저장됨
        String redisKey = "refreshToken:" + user.getId();
        String refreshToken = redisTemplate.opsForValue().get(redisKey);
        assert refreshToken != null;
    }

    @Test
    void 로그인_실패_비밀번호_틀림() throws Exception {
        // Given: 회원가입된 사용자
        User user = authService.register("홍길동", "hong@company.com", "password123", "STUDENT");

        // When: 잘못된 비밀번호로 로그인 시도
        // Then: 상태 401 Unauthorized
        mockMvc.perform(post("/api/auth/login")
                        .param("email", "hong@company.com")
                        .param("password", "wrongpassword")
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void 리프레시토큰_성공_재발급() {
        // Given: 회원가입 및 로그인으로 받은 refreshToken
        User user = authService.register("홍길동", "hong@company.com", "password123", "STUDENT");
        AuthResponse authResponse = authService.login("hong@company.com", "password123");

        // When: refreshToken으로 액세스 토큰 재발급
        String newAccessToken = authService.refreshAccessToken(authResponse.getRefreshToken());

        // Then: 새 액세스 토큰이 존재해야 함
        assert newAccessToken != null && !newAccessToken.isEmpty();
    }

    @Test
    void 리프레시토큰_실패_만료또는_잘못된토큰() {
        // Given: 회원가입된 사용자와 잘못된 refreshToken
        User user = authService.register("홍길동", "hong@company.com", "password123", "STUDENT");
        String invalidToken = "invalid-token";

        // When & Then: refreshToken 재발급 시도 시 예외 발생
        try {
            authService.refreshAccessToken(invalidToken);
            assert false; // 예외가 발생해야 함
        } catch (Exception e) {
            assert true;
        }
    }
}