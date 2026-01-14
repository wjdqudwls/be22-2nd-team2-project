-- ================================================
-- Member Service Database Schema
-- ================================================
-- 담당자: 김태형
-- 서비스: member-service (Port 8081)
-- 데이터베이스: next_page_member
-- ================================================

USE next_page_member;

-- 1. 사용자 테이블 (Users)
-- JWT 인증, 회원 관리, Soft Delete 지원
CREATE TABLE `users` (
    `user_id` INT NOT NULL AUTO_INCREMENT COMMENT '사용자 ID (PK)',
    `user_email` VARCHAR(100) NOT NULL COMMENT '로그인 이메일 (UK)',
    `user_pw` VARCHAR(255) NOT NULL COMMENT 'BCrypt 암호화된 비밀번호',
    `user_nicknm` VARCHAR(50) NOT NULL COMMENT '닉네임 (UK)',
    `user_role` VARCHAR(20) NOT NULL DEFAULT 'USER' COMMENT '권한: USER, ADMIN',
    `user_status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '상태: ACTIVE, DELETED',
    `left_at` DATETIME NULL COMMENT '탈퇴일시 (Soft Delete)',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '가입일시',
    `updated_at` DATETIME NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',

    PRIMARY KEY (`user_id`),
    UNIQUE KEY `uk_users_email` (`user_email`),
    UNIQUE KEY `uk_users_nicknm` (`user_nicknm`),
    INDEX `idx_users_status` (`user_status`),
    INDEX `idx_users_email_status` (`user_email`, `user_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='회원 정보 테이블 (Member Service)';

-- 2. 리프레시 토큰 테이블 (Optional - Redis 대안)
-- JWT Refresh Token 저장 (Redis 사용 시 생략 가능)
CREATE TABLE `refresh_tokens` (
    `token_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '토큰 ID (PK)',
    `user_id` INT NOT NULL COMMENT '사용자 ID (FK)',
    `refresh_token` VARCHAR(500) NOT NULL COMMENT 'Refresh Token',
    `expires_at` DATETIME NOT NULL COMMENT '만료 시각',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '발급 시각',

    PRIMARY KEY (`token_id`),
    UNIQUE KEY `uk_refresh_token` (`refresh_token`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_expires_at` (`expires_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='JWT 리프레시 토큰 저장 (Redis 대안)';

-- 샘플 데이터 삽입 (개발/테스트용)
-- 비밀번호: "password123" (BCrypt 해시)
INSERT INTO users (user_email, user_pw, user_nicknm, user_role, user_status, created_at)
VALUES
    ('admin@nextpage.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '관리자', 'ADMIN', 'ACTIVE', NOW()),
    ('user1@nextpage.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '소설가1', 'USER', 'ACTIVE', NOW()),
    ('user2@nextpage.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '소설가2', 'USER', 'ACTIVE', NOW()),
    ('user3@nextpage.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '소설가3', 'USER', 'ACTIVE', NOW());

-- 스키마 확인
SHOW TABLES;
DESCRIBE users;
