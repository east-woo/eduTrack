package com.eastwoo.toy.edutrack.auth.email;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendInviteEmail(String email, String token) {
        String inviteUrl = "http://localhost:8080/invite?token=" + token; // 필요하면 프론트 주소로 변경

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom(from);
        message.setSubject("[eduTrack] 강사 초대 메일");
        message.setText("""
                안녕하세요.

                아래 링크를 통해 강사 초대를 완료해주세요:
                %s

                (본 메일은 발신 전용입니다.)
                """.formatted(inviteUrl));

        mailSender.send(message);
    }
}