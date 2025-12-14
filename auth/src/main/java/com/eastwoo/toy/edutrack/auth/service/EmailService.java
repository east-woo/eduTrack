package com.eastwoo.toy.edutrack.auth.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    public void sendInviteEmail(String email, String token) {
        System.out.println("초대 이메일 발송 → " + email + ", token = " + token);
        // 실제 구현은 SMTP 연결 로직
    }
}