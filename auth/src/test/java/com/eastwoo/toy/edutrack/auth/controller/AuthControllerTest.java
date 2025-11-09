package com.eastwoo.toy.edutrack.auth.controller;


import com.eastwoo.toy.edutrack.auth.entity.User;
import com.eastwoo.toy.edutrack.auth.repository.UserRepository;
import com.eastwoo.toy.edutrack.auth.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.notNullValue;
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
    private StringRedisTemplate redisTemplate;

    @Autowired
    private AuthService authService;

    @BeforeEach
    void setUp() {
        // DB 초기화
        userRepository.deleteAll();
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    void testRegister_Success() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .param("name", "홍길동")
                        .param("email", "hong@company.com")
                        .param("password", "password123")
                        .param("role", "STUDENT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name").value("홍길동"))
                .andExpect(jsonPath("$.email").value("hong@company.com"));
    }

    @Test
    void testLogin_Success_And_RedisRefreshToken() throws Exception {
        // 먼저 회원가입
        User user = authService.register("홍길동", "hong@company.com", "password123", "STUDENT");

        // 로그인 요청
        String response = mockMvc.perform(post("/api/auth/login")
                        .param("email", "hong@company.com")
                        .param("password", "password123"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // JWT 토큰 반환 확인
        assert response != null && !response.isEmpty();

        // Redis에 RefreshToken 존재 확인
        String redisKey = "refreshToken:" + user.getId();
        String refreshToken = redisTemplate.opsForValue().get(redisKey);
        assert refreshToken != null;
    }

    @Test
    void testLogin_Failure_WrongPassword() throws Exception {
        User user = authService.register("홍길동", "hong@company.com", "password123", "STUDENT");

        mockMvc.perform(post("/api/auth/login")
                        .param("email", "hong@company.com")
                        .param("password", "wrongpassword"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testRefreshToken_Success() throws Exception {
        User user = authService.register("홍길동", "hong@company.com", "password123", "STUDENT");
        String refreshToken = authService.login("hong@company.com", "password123"); // Redis에 저장됨

        // refreshToken 재발급 호출
        String newAccessToken = authService.refreshAccessToken(refreshToken);
        assert newAccessToken != null && !newAccessToken.isEmpty();
    }

    @Test
    void testRefreshToken_Failure_ExpiredOrInvalid() {
        User user = authService.register("홍길동", "hong@company.com", "password123", "STUDENT");

        String invalidToken = "invalid-token";

        try {
            authService.refreshAccessToken(invalidToken);
            assert false; // 예외가 발생해야 함
        } catch (Exception e) {
            assert true;
        }
    }
}