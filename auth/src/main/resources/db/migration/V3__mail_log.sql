-- =========================
-- Mail Send Success Log
-- =========================
CREATE TABLE mail_send_success_log (
                                       id BIGSERIAL PRIMARY KEY,
                                       email VARCHAR(255) NOT NULL,
                                       subject VARCHAR(255) NOT NULL,
                                       event_type VARCHAR(50) NOT NULL,
                                       sent_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_mail_success_email
    ON mail_send_success_log (email);

CREATE INDEX idx_mail_success_event_type
    ON mail_send_success_log (event_type);


-- =========================
-- Mail Send Fail Log
-- =========================
CREATE TABLE mail_send_fail_log (
                                    id BIGSERIAL PRIMARY KEY,
                                    email VARCHAR(255) NOT NULL,
                                    subject VARCHAR(255) NOT NULL,
                                    error_message VARCHAR(2000),
                                    failed_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_mail_fail_email
    ON mail_send_fail_log (email);

CREATE INDEX idx_mail_fail_failed_at
    ON mail_send_fail_log (failed_at);
