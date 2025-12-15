package com.eastwoo.toy.edutrack.auth.email.service;

import com.eastwoo.toy.edutrack.auth.email.entity.MailSendSuccessLog;
import com.eastwoo.toy.edutrack.auth.email.repository.MailSendSuccessLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MailSuccessLogService {

    private final MailSendSuccessLogRepository repository;

    @Transactional
    public void save(String email, String subject, String eventType) {
        repository.save(
                MailSendSuccessLog.builder()
                        .email(email)
                        .subject(subject)
                        .eventType(eventType)
                        .sentAt(LocalDateTime.now())
                        .build()
        );
    }
}