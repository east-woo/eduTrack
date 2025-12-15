package com.eastwoo.toy.edutrack.auth.controller;

import com.eastwoo.toy.edutrack.auth.instructor.controller.AdminInstructorController;
import com.eastwoo.toy.edutrack.auth.instructor.service.InstructorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminInstructorController.class)
class AdminInstructorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InstructorService instructorService;

    @Test
    @DisplayName("비회원 강사 가입 요청 승인 API 테스트")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void approveSignupTest() throws Exception {
        // Given
        doNothing().when(instructorService).approveSignup(1L);

        // When & Then
        mockMvc.perform(post("/api/admin/instructors/signup-requests/1/approve")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("비회원 강사 가입 요청 거절 API 테스트")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void rejectSignupTest() throws Exception {
        // Given
        doNothing().when(instructorService).rejectSignup(1L);

        // When & Then
        mockMvc.perform(post("/api/admin/instructors/signup-requests/1/reject")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("학생 → 강사 승급 승인 API 테스트")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void approvePromotionTest() throws Exception {
        // Given
        doNothing().when(instructorService).approvePromotion(1L);

        // When & Then
        mockMvc.perform(post("/api/admin/instructors/promotion-requests/1/approve")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("학생 → 강사 승급 거절 API 테스트")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void rejectPromotionTest() throws Exception {
        // Given
        doNothing().when(instructorService).rejectPromotion(1L);

        // When & Then
        mockMvc.perform(post("/api/admin/instructors/promotion-requests/1/reject")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("관리자 → 강사 초대 API 테스트")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void inviteTeacherTest() throws Exception {
        // Given
        doNothing().when(instructorService).inviteTeacher(any());

        // When & Then
        mockMvc.perform(post("/api/admin/instructors/invite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"teacher@example.com\"}")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}