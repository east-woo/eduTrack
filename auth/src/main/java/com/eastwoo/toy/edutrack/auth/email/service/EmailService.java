package com.eastwoo.toy.edutrack.auth.email.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final MailFailLogService mailFailLogService;
    private final MailSuccessLogService mailSuccessLogService;

    @Value("${spring.mail.username}")
    private String from;

    @Async
    public void sendInviteEmail(String email, String token) {

        String subject = "[eduTrack] 강사 초대 메일";
        String eventType = "INVITE_TEACHER";

        try {
            String inviteUrl = "http://localhost:8080/invite?token=" + token;

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setFrom(from);
            message.setSubject(subject);
            message.setText("""
                    안녕하세요.

                    아래 링크를 통해 강사 초대를 완료해주세요:
                    %s

                    (본 메일은 발신 전용입니다.)
                    """.formatted(inviteUrl));

            mailSender.send(message);

            // ✅ 성공 로그
            mailSuccessLogService.save(email, subject, eventType);

            log.info("[MAIL-SUCCESS] email={}", email);

        } catch (Exception e) {
            // ❌ 실패 로그
            mailFailLogService.save(email, subject, e);

            log.error("[MAIL-FAIL] email={}", email, e);
        }
    }
}