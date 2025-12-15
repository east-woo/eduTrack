package com.eastwoo.toy.edutrack.auth.instructor.controller;

import com.eastwoo.toy.edutrack.auth.global.dto.ApiResponse;
import com.eastwoo.toy.edutrack.auth.instructor.dto.InviteTeacherRequest;
import com.eastwoo.toy.edutrack.auth.instructor.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/* 관리자 - 강사 관리 */
@RestController
@RequestMapping("/api/admin/instructors")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminInstructorController {

    private final InstructorService instructorService;

    /* 강사 가입 요청 승인 (비회원) */
    @PostMapping("/signup-requests/{requestId}/approve")
    public ResponseEntity<ApiResponse<Void>> approveSignup(@PathVariable Long requestId) {
        instructorService.approveSignup(requestId);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @PostMapping("/signup-requests/{requestId}/reject")
    public ResponseEntity<ApiResponse<Void>> rejectSignup(@PathVariable Long requestId) {
        instructorService.rejectSignup(requestId);
        return ResponseEntity.ok(ApiResponse.success());
    }

    /* 학생 → 강사 승급 승인 */
    @PostMapping("/promotion-requests/{requestId}/approve")
    public ResponseEntity<ApiResponse<Void>> approvePromotion(@PathVariable Long requestId) {
        instructorService.approvePromotion(requestId);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @PostMapping("/promotion-requests/{requestId}/reject")
    public ResponseEntity<ApiResponse<Void>> rejectPromotion(@PathVariable Long requestId) {
        instructorService.rejectPromotion(requestId);
        return ResponseEntity.ok(ApiResponse.success());
    }

    /* 관리자 → 강사 초대 */
    @PostMapping("/invite")
    public ResponseEntity<ApiResponse<Void>> inviteTeacher(@RequestBody InviteTeacherRequest request) {
        instructorService.inviteTeacher(request);
        return ResponseEntity.ok(ApiResponse.success());
    }
}