package com.eastwoo.toy.edutrack.auth.email.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MailSendSuccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String subject;

    /**
     *
     * 어떤 도메인 이벤트로 보냈는지
     * ex) INVITE_TEACHER, SIGNUP_VERIFY
     */
    private String eventType;

    private LocalDateTime sentAt;
}