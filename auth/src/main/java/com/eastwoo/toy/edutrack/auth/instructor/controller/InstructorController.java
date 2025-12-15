package com.eastwoo.toy.edutrack.auth.instructor.controller;

import com.eastwoo.toy.edutrack.auth.global.dto.ApiResponse;
import com.eastwoo.toy.edutrack.auth.instructor.dto.InstructorRegisterResponse;
import com.eastwoo.toy.edutrack.auth.instructor.dto.TeacherRegisterRequest;
import com.eastwoo.toy.edutrack.auth.instructor.service.InstructorService;
import com.eastwoo.toy.edutrack.auth.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/* 강사 관련 API */
@RestController
@RequestMapping("/api/instructors")
@RequiredArgsConstructor
public class InstructorController {

    private final InstructorService instructorService;

    /* 초대 토큰 기반 강사가 직접 회원가입 */
    @PreAuthorize("permitAll()")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<InstructorRegisterResponse>> register(
            @RequestBody TeacherRegisterRequest request) {

        User user = instructorService.registerTeacher(request);
        InstructorRegisterResponse response = InstructorRegisterResponse.from(user);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}