# Next-Page MSA Database Setup Guide

## 개요
이 디렉토리는 Next-Page 프로젝트의 Monolithic 아키텍처를 MSA로 전환하기 위한 데이터베이스 분리 스크립트를 포함합니다.

## 데이터베이스 구조

```
Monolithic (next_page_db)
    ↓ 분리
    ├─ next_page_member     (Member Service)
    ├─ next_page_story      (Story Service)
    └─ next_page_reaction   (Reaction Service)
```

## 실행 순서

### 1단계: 데이터베이스 생성
```bash
mysql -u root -p < 01-create-databases.sql
```

**생성되는 항목:**
- 데이터베이스 3개 생성
- 서비스별 전용 계정 생성
- 권한 부여

**생성된 계정:**
| 계정 | 비밀번호 | 접근 DB |
|------|---------|---------|
| member_service | member_pw_2026 | next_page_member |
| story_service | story_pw_2026 | next_page_story |
| reaction_service | reaction_pw_2026 | next_page_reaction |

> ⚠️ **운영 환경**: 비밀번호를 반드시 변경하세요!

---

### 2단계: Member Service 스키마 생성
```bash
mysql -u root -p < 02-member-service-schema.sql
```

**생성되는 테이블:**
- `users` (회원 정보)
- `refresh_tokens` (JWT 리프레시 토큰, Redis 대안)

**샘플 데이터:**
- 관리자 계정: admin@nextpage.com / password123
- 일반 사용자 3명 (user1, user2, user3)

---

### 3단계: Story Service 스키마 생성
```bash
mysql -u root -p < 03-story-service-schema.sql
```

**생성되는 테이블:**
- `categories` (소설 카테고리)
- `books` (릴레이 소설 방)
- `sentences` (소설 문장)

**초기 데이터:**
- 카테고리 6개 (스릴러, 로맨스, 판타지, SF, 미스터리, 일상/드라마)
- 샘플 소설 3개 + 문장 4개

**중요 설계:**
- `books` 테이블의 `writer_id`는 Member Service의 `user_id`를 참조 (외래키 X)
- Book Aggregate Root 패턴 적용

---

### 4단계: Reaction Service 스키마 생성
```bash
mysql -u root -p < 04-reaction-service-schema.sql
```

**생성되는 테이블:**
- `comments` (댓글/대댓글)
- `book_votes` (소설 투표)
- `sentence_votes` (문장 투표)

**샘플 데이터:**
- 댓글 5개 (대댓글 포함)
- 소설 투표 6개
- 문장 투표 5개

**중요 제약:**
- 1인 1투표: `uk_book_voter`, `uk_sentence_voter` 유니크 키
- 계층형 댓글: `parent_id` 자기 참조

---

## 일괄 실행 스크립트 (Bash)

### Linux/Mac
```bash
#!/bin/bash
DB_USER="root"
DB_PASS="your_password"

echo "1. 데이터베이스 생성..."
mysql -u $DB_USER -p$DB_PASS < 01-create-databases.sql

echo "2. Member Service 스키마 생성..."
mysql -u $DB_USER -p$DB_PASS < 02-member-service-schema.sql

echo "3. Story Service 스키마 생성..."
mysql -u $DB_USER -p$DB_PASS < 03-story-service-schema.sql

echo "4. Reaction Service 스키마 생성..."
mysql -u $DB_USER -p$DB_PASS < 04-reaction-service-schema.sql

echo "✅ 모든 데이터베이스 설정 완료!"
```

### Windows (PowerShell)
```powershell
$DB_USER = "root"
$DB_PASS = "your_password"

Write-Host "1. 데이터베이스 생성..." -ForegroundColor Green
mysql -u $DB_USER -p$DB_PASS < 01-create-databases.sql

Write-Host "2. Member Service 스키마 생성..." -ForegroundColor Green
mysql -u $DB_USER -p$DB_PASS < 02-member-service-schema.sql

Write-Host "3. Story Service 스키마 생성..." -ForegroundColor Green
mysql -u $DB_USER -p$DB_PASS < 03-story-service-schema.sql

Write-Host "4. Reaction Service 스키마 생성..." -ForegroundColor Green
mysql -u $DB_USER -p$DB_PASS < 04-reaction-service-schema.sql

Write-Host "✅ 모든 데이터베이스 설정 완료!" -ForegroundColor Cyan
```

---

## 데이터베이스 구조 검증

### 생성된 DB 확인
```sql
SHOW DATABASES LIKE 'next_page_%';
```

**Expected Output:**
```
+-------------------------+
| Database                |
+-------------------------+
| next_page_member        |
| next_page_reaction      |
| next_page_story         |
+-------------------------+
```

### 테이블 개수 확인
```sql
-- Member Service (2개)
USE next_page_member;
SHOW TABLES;  -- users, refresh_tokens

-- Story Service (3개)
USE next_page_story;
SHOW TABLES;  -- categories, books, sentences

-- Reaction Service (3개)
USE next_page_reaction;
SHOW TABLES;  -- comments, book_votes, sentence_votes
```

### 외래키 제약 확인
```sql
-- Story Service
SELECT
    TABLE_NAME,
    CONSTRAINT_NAME,
    REFERENCED_TABLE_NAME
FROM information_schema.KEY_COLUMN_USAGE
WHERE TABLE_SCHEMA = 'next_page_story'
AND REFERENCED_TABLE_NAME IS NOT NULL;
```

---

## 서비스 간 데이터 참조 전략

### MSA에서 외래키를 삭제한 이유
- **독립성**: 각 서비스가 독립적으로 배포/확장 가능
- **결합도 감소**: DB 레벨 의존성 제거
- **유연성**: 서비스 간 통신 방식 변경 용이

### ID 참조 방식

| 참조 | 방법 | 예시 |
|------|------|------|
| Book → Member | `writer_id` INT 저장 | OpenFeign으로 사용자 정보 조회 |
| Comment → Book | `book_id` INT 저장 | OpenFeign으로 소설 정보 검증 |
| Vote → Member | `voter_id` INT 저장 | JWT에서 사용자 ID 추출 |

### 데이터 정합성 보장 방법

1. **애플리케이션 레벨 검증**
   - 서비스 레이어에서 Feign Client로 ID 존재 여부 확인
   - 트랜잭션 실패 시 롤백 (SAGA 패턴)

2. **이벤트 기반 동기화**
   - Member 삭제 시 → Kafka 이벤트 발행 → Story/Reaction 서비스 수신
   - 비동기 처리로 성능 유지

3. **캐싱 전략**
   - Redis에 사용자 기본 정보 캐싱
   - Member Service 장애 시 캐시에서 조회

---

## 마이그레이션 체크리스트

### 데이터 이관 (Monolithic → MSA)

- [ ] **Member 데이터**
  ```sql
  INSERT INTO next_page_member.users
  SELECT * FROM next_page_db.users;
  ```

- [ ] **Story 데이터**
  ```sql
  INSERT INTO next_page_story.categories
  SELECT * FROM next_page_db.categories;

  INSERT INTO next_page_story.books
  SELECT * FROM next_page_db.books;

  INSERT INTO next_page_story.sentences
  SELECT * FROM next_page_db.sentences;
  ```

- [ ] **Reaction 데이터**
  ```sql
  INSERT INTO next_page_reaction.comments
  SELECT * FROM next_page_db.comments;

  INSERT INTO next_page_reaction.book_votes
  SELECT * FROM next_page_db.book_votes;

  INSERT INTO next_page_reaction.sentence_votes
  SELECT * FROM next_page_db.sentence_votes;
  ```

### 애플리케이션 설정

- [ ] **application.yml 업데이트**
  - member-service: `jdbc:mariadb://localhost:3306/next_page_member`
  - story-service: `jdbc:mariadb://localhost:3306/next_page_story`
  - reaction-service: `jdbc:mariadb://localhost:3306/next_page_reaction`

- [ ] **OpenFeign 클라이언트 구현**
  - Story Service → Member Service (사용자 정보 조회)
  - Reaction Service → Member Service (투표자 정보 조회)
  - Reaction Service → Story Service (소설/문장 검증)

- [ ] **JWT 토큰 공유**
  - Gateway에서 토큰 검증
  - `X-User-Id` 헤더로 사용자 ID 전달

---

## 롤백 가이드

### 데이터베이스 삭제
```sql
DROP DATABASE IF EXISTS next_page_member;
DROP DATABASE IF EXISTS next_page_story;
DROP DATABASE IF EXISTS next_page_reaction;

DROP USER IF EXISTS 'member_service'@'localhost';
DROP USER IF EXISTS 'story_service'@'localhost';
DROP USER IF EXISTS 'reaction_service'@'localhost';

FLUSH PRIVILEGES;
```

### Monolithic 복구
```bash
# 기존 next_page_db 백업 복원
mysql -u root -p next_page_db < backup.sql
```

---

## 성능 고려사항

### 인덱스 전략
- **필수 인덱스**: PK, UK, FK 역할 컬럼
- **조회 인덱스**: `idx_created_at DESC`, `idx_status`
- **복합 인덱스**: `idx_category_status`, `idx_book_created`

### 쿼리 최적화
```sql
-- ❌ Bad: N+1 문제
SELECT * FROM books;
-- 각 book마다 member-service 호출 (100번)

-- ✅ Good: 배치 조회
SELECT * FROM books LIMIT 20;
-- IN 쿼리로 member-service 1번 호출
-- GET /api/members/batch?ids=1,2,3,...
```

### 연결 풀 설정
```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 10
      minimum-idle: 5
      connection-timeout: 20000
```

---

## 문제 해결 (Troubleshooting)

### 1. "Access denied for user"
**원인**: 사용자 권한 부족
**해결**:
```sql
GRANT ALL PRIVILEGES ON next_page_member.* TO 'member_service'@'localhost';
FLUSH PRIVILEGES;
```

### 2. "Unknown database"
**원인**: 데이터베이스 미생성
**해결**:
```sql
CREATE DATABASE next_page_member;
```

### 3. "Duplicate entry"
**원인**: 샘플 데이터 중복 실행
**해결**:
```sql
-- 테이블 초기화 후 재실행
TRUNCATE TABLE users;
```

### 4. 외래키 제약 위반 (Monolithic 이관 시)
**원인**: 참조 무결성 깨짐
**해결**:
```sql
-- 데이터 정합성 검증
SELECT b.* FROM next_page_db.books b
LEFT JOIN next_page_db.users u ON b.writer_id = u.user_id
WHERE u.user_id IS NULL;
-- 결과가 있으면 해당 데이터 수정 후 이관
```

---

## 참고 자료

- [Monolithic 스키마](../../next-page/src/main/resources/script.sql)
- [MSA 아키텍처 가이드](../DEVELOPER_GUIDE.md#5--migration-to-msa-microservices-guide)
- [Spring Data JPA 설정](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [MyBatis Multi-DB 설정](https://mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/)

---

**Last Updated:** 2026-01-14
**Maintainer:** Next-Page Team
