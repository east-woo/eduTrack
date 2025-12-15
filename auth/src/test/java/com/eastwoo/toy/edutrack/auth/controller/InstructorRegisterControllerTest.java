package com.eastwoo.toy.edutrack.auth.controller;

import com.eastwoo.toy.edutrack.auth.instructor.controller.InstructorController;
import com.eastwoo.toy.edutrack.auth.instructor.dto.TeacherRegisterRequest;
import com.eastwoo.toy.edutrack.auth.instructor.service.InstructorService;
import com.eastwoo.toy.edutrack.auth.user.entity.User;
import com.eastwoo.toy.edutrack.auth.user.enumtype.UserRole;
import com.eastwoo.toy.edutrack.auth.user.enumtype.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InstructorController.class)
@AutoConfigureMockMvc(addFilters = false)
class InstructorRegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InstructorService instructorService;


    @Test
    @DisplayName("강사 회원가입 API 테스트")
    void registerTest() throws Exception {

        // Given
        TeacherRegisterRequest request = new TeacherRegisterRequest("홍길동", "password", "token");
        User user = User.builder()
                .id(1L)
                .name("홍길동")
                .email("teacher@example.com")
                .password("encoded")
                .role(UserRole.TEACHER)
                .status(UserStatus.ACTIVE)
                .build();
        when(instructorService.registerTeacher(any())).thenReturn(user);

        // When & Then
        mockMvc.perform(post("/api/instructors/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name":"홍길동",
                                    "password":"password",
                                    "token":"token"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.email").value("teacher@example.com"))
                .andExpect(jsonPath("$.data.role").value("TEACHER"));
    }
}