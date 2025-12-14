package com.eastwoo.toy.edutrack.auth.controller;

import com.eastwoo.toy.edutrack.auth.entity.InviteToken;
import com.eastwoo.toy.edutrack.auth.entity.User;
import com.eastwoo.toy.edutrack.auth.enumtype.UserRole;
import com.eastwoo.toy.edutrack.auth.repository.InviteTokenRepository;
import com.eastwoo.toy.edutrack.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class InstructorRegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InviteTokenRepository inviteTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        inviteTokenRepository.deleteAll();
    }

    @Test
    void 초대토큰으로_강사_회원가입_성공() throws Exception {
        // given: 초대 토큰 생성
        InviteToken invite = InviteToken.builder()
                .email("teacher@test.com")
                .token("invite-token-123")
                .role(UserRole.TEACHER)
                .expiresAt(LocalDateTime.now().plusHours(1))
                .build();

        inviteTokenRepository.save(invite);

        // when & then
        mockMvc.perform(post("/api/instructors/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "token": "invite-token-123",
                                  "name": "강사님",
                                  "password": "password123"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("teacher@test.com"))
                .andExpect(jsonPath("$.role").value("TEACHER"));

        // 유저 생성 확인
        User user = userRepository.findByEmail("teacher@test.com").orElseThrow();
        assertThat(user.getRole()).isEqualTo(UserRole.TEACHER);

        // 토큰 삭제 확인
        assertThat(inviteTokenRepository.findByToken("invite-token-123")).isEmpty();
    }

    @Test
    void 만료된_초대토큰_회원가입_실패() throws Exception {
        InviteToken invite = InviteToken.builder()
                .email("teacher@test.com")
                .token("expired-token")
                .role(UserRole.TEACHER)
                .expiresAt(LocalDateTime.now().minusMinutes(1))
                .build();

        inviteTokenRepository.save(invite);

        mockMvc.perform(post("/api/instructors/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "token": "expired-token",
                              "name": "강사",
                              "password": "password"
                            }
                            """))
                .andExpect(status().isBadRequest());
    }
}