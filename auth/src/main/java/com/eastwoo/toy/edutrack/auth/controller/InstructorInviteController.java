package com.eastwoo.toy.edutrack.auth.controller;

import com.eastwoo.toy.edutrack.auth.dto.InviteTeacherRequest;
import com.eastwoo.toy.edutrack.auth.enumtype.UserRole;
import com.eastwoo.toy.edutrack.auth.service.EmailService;
import com.eastwoo.toy.edutrack.auth.service.InviteTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/instructors")
public class InstructorInviteController {

    private final InviteTokenService inviteTokenService;
    private final EmailService emailService;

    @PostMapping("/invite")
    public String inviteTeacher(@RequestBody InviteTeacherRequest request) {

        String token = inviteTokenService.createInvite(request.email(), UserRole.TEACHER);

        emailService.sendInviteEmail(request.email(), token);

        return "강사 초대 이메일 발송 완료";
    }
}