-- ================================================
-- MSA Database Setup Script
-- Next-Page 프로젝트 마이크로서비스 데이터베이스 생성
-- ================================================
-- 작성일: 2026-01-14
-- 목적: Monolithic DB를 3개의 독립된 DB로 분리
-- ================================================

-- 1. Member Service Database
CREATE DATABASE IF NOT EXISTS `next_page_member`
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- 2. Story Service Database
CREATE DATABASE IF NOT EXISTS `next_page_story`
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- 3. Reaction Service Database
CREATE DATABASE IF NOT EXISTS `next_page_reaction`
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- 사용자 생성 및 권한 부여 (로컬 개발용)
-- 운영 환경에서는 서비스별로 별도 계정 생성 권장

-- Member Service User
CREATE USER IF NOT EXISTS 'member_service'@'localhost' IDENTIFIED BY 'member_pw_2026';
GRANT ALL PRIVILEGES ON next_page_member.* TO 'member_service'@'localhost';

-- Story Service User
CREATE USER IF NOT EXISTS 'story_service'@'localhost' IDENTIFIED BY 'story_pw_2026';
GRANT ALL PRIVILEGES ON next_page_story.* TO 'story_service'@'localhost';

-- Reaction Service User
CREATE USER IF NOT EXISTS 'reaction_service'@'localhost' IDENTIFIED BY 'reaction_pw_2026';
GRANT ALL PRIVILEGES ON next_page_reaction.* TO 'reaction_service'@'localhost';

FLUSH PRIVILEGES;

-- 생성 확인
SHOW DATABASES LIKE 'next_page_%';
