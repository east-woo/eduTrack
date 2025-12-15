package com.eastwoo.toy.edutrack.auth.notification.service;

import com.eastwoo.toy.edutrack.auth.email.service.EmailService;
import com.eastwoo.toy.edutrack.auth.notification.port.NotificationPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailNotificationService implements NotificationPort {

    private final EmailService emailService;

    @Override
    public void sendInviteEmail(String email, String token) {
        emailService.sendInviteEmail(email, token);
    }
}