package com.eastwoo.toy.edutrack.auth.controller;

import com.eastwoo.toy.edutrack.auth.instructor.dto.InstructorSignupRequestDto;
import com.eastwoo.toy.edutrack.auth.instructor.service.InstructorService;
import com.eastwoo.toy.edutrack.auth.user.controller.AuthController;
import com.eastwoo.toy.edutrack.auth.user.dto.AuthResponse;
import com.eastwoo.toy.edutrack.auth.user.entity.User;
import com.eastwoo.toy.edutrack.auth.user.enumtype.UserRole;
import com.eastwoo.toy.edutrack.auth.user.enumtype.UserStatus;
import com.eastwoo.toy.edutrack.auth.user.service.AuthService;
import com.eastwoo.toy.edutrack.auth.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private InstructorService instructorService;

    @Test
    @DisplayName("로그인 API 테스트")
    void loginTest() throws Exception {
        // Given
        String email = "user@example.com";
        String password = "password";
        AuthResponse authResponse = new AuthResponse("access-token", "refresh-token");
        when(authService.login(email, password)).thenReturn(authResponse);

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .param("email", email)
                        .param("password", password).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").value("access-token"))
                .andExpect(jsonPath("$.data.refreshToken").value("refresh-token"));
    }

    @Test
    @DisplayName("회원가입 API 테스트")
    void registerTest() throws Exception {
        // Given
        String email = "user@example.com";
        String name = "홍길동";
        String password = "password";

        User user = User.builder()
                .id(1L)
                .name(name)
                .email(email)
                .password("encodedPassword")
                .role(UserRole.STUDENT)
                .status(UserStatus.ACTIVE)
                .build();

        when(userService.register(name, email, password, UserRole.STUDENT)).thenReturn(user);

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                        .param("name", name)
                        .param("email", email)
                        .param("password", password).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.email").value(email));
    }

    @Test
    @DisplayName("강사 가입 요청 API 테스트")
    void requestInstructorSignupTest() throws Exception {
        // Given
        InstructorSignupRequestDto dto = new InstructorSignupRequestDto("홍길동", "teacher@example.com", "message");
        doNothing().when(instructorService).requestSignup(any());

        // When & Then
        mockMvc.perform(post("/api/auth/instructor-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name":"홍길동",
                                    "email":"teacher@example.com",
                                    "message":"message"
                                }
                                """).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}