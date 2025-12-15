package com.eastwoo.toy.edutrack.auth.user.controller;

import com.eastwoo.toy.edutrack.auth.global.dto.ApiResponse;
import com.eastwoo.toy.edutrack.auth.user.dto.AuthResponse;
import com.eastwoo.toy.edutrack.auth.instructor.dto.InstructorSignupRequestDto;
import com.eastwoo.toy.edutrack.auth.user.entity.User;
import com.eastwoo.toy.edutrack.auth.user.enumtype.UserRole;
import com.eastwoo.toy.edutrack.auth.user.service.AuthService;
import com.eastwoo.toy.edutrack.auth.instructor.service.InstructorService;
import com.eastwoo.toy.edutrack.auth.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/* 인증 (로그인 / 회원가입) */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final InstructorService instructorService;

    @PreAuthorize("permitAll()")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @RequestParam String email,
            @RequestParam String password) {

        AuthResponse response = authService.login(email, password);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password) {

        User user = userService.register(name, email, password, UserRole.STUDENT);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    /* 비회원 → 강사 가입 요청 */
    @PreAuthorize("permitAll()")
    @PostMapping("/instructor-request")
    public ResponseEntity<ApiResponse<Void>> requestInstructorSignup(
            @RequestBody InstructorSignupRequestDto request) {

        instructorService.requestSignup(request);
        return ResponseEntity.ok(ApiResponse.success());
    }
}