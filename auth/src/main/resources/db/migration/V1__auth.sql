CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       email VARCHAR(150) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(50) NOT NULL DEFAULT 'STUDENT',
                       status VARCHAR(50) DEFAULT 'ACTIVE',
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);