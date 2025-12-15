-- users 테이블 (기존 V1과 동일 내용, 혹시 재생성 필요 시)
CREATE TABLE IF NOT EXISTS users (
                                     id BIGSERIAL PRIMARY KEY,
                                     name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'STUDENT',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

-- InviteToken 테이블
CREATE TABLE IF NOT EXISTS invite_token (
                                            id BIGSERIAL PRIMARY KEY,
                                            email VARCHAR(150) NOT NULL,
    token VARCHAR(255) UNIQUE NOT NULL,
    expires_at TIMESTAMP,
    role VARCHAR(50) NOT NULL
    );

-- Instructor Signup Request 테이블
CREATE TABLE IF NOT EXISTS instructor_signup_requests (
                                                          id BIGSERIAL PRIMARY KEY,
                                                          name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    message VARCHAR(500),
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

-- Instructor Promotion Request 테이블
CREATE TABLE IF NOT EXISTS instructor_promotion_requests (
                                                             id BIGSERIAL PRIMARY KEY,
                                                             user_id BIGINT NOT NULL,
                                                             message VARCHAR(500),
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES users(id)
    );