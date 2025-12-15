package com.eastwoo.toy.edutrack.auth.email.service;

import com.eastwoo.toy.edutrack.auth.email.entity.MailSendFailLog;
import com.eastwoo.toy.edutrack.auth.email.repository.MailSendFailLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MailFailLogService {

    private final MailSendFailLogRepository repository;

    @Transactional
    public void save(String email, String subject, Exception e) {
        repository.save(
                MailSendFailLog.builder()
                        .email(email)
                        .subject(subject)
                        .errorMessage(e.getMessage())
                        .failedAt(LocalDateTime.now())
                        .build()
        );
    }
}