package com.eastwoo.toy.edutrack.auth.email.repository;

import com.eastwoo.toy.edutrack.auth.email.entity.MailSendFailLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailSendFailLogRepository
        extends JpaRepository<MailSendFailLog, Long> {
}