package com.eastwoo.toy.edutrack.auth.email.repository;

import com.eastwoo.toy.edutrack.auth.email.entity.MailSendSuccessLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailSendSuccessLogRepository
        extends JpaRepository<MailSendSuccessLog, Long> {
}