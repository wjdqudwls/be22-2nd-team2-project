# 🧑‍💻 Next Page Developer Guide

본 가이드는 **Next Page** 프로젝트의 개발자들이 참고할 수 있는 **API 테스트, 기능 명세, 개발 가이드** 문서입니다.  
각 담당자는 본인이 개발한 파트에 대한 설명을 참고하고, 새로운 기능 개발 시 이 문서를 업데이트해 주세요.

---

## 📑 Table of Contents (목차)
1.  [Coding Conventions & Patterns](#1-%EF%B8%8F-coding-conventions--patterns)
2.  [Getting Started](#2--getting-started)
3.  [API Testing Guide](#3--api-testing-guide)
4.  [Module-Specific Guide](#4-%EF%B8%8F-module-specific-guide-담당자별-가이드)
5.  [Real-time System Flow](#5--real-time-system-flow)
6.  [Migration to MSA Guide](#6--migration-to-msa-microservices-guide)
7.  [Testing Strategy](#7--testing-strategy)
8.  [Troubleshooting](#8-%EF%B8%8F-troubleshooting)

---

## 1. 🏛️ Coding Conventions & Patterns

### 1-1. CQRS & Architecture
*   **Command:** 데이터 상태를 변경하는 모든 로직은 `command` 패키지에서 **JPA**를 사용해 처리합니다.
    *   Entity의 비즈니스 메서드를 통해 상태를 변경합니다 (Setter 지양).
*   **Query:** 데이터 조회가 주 목적(화면 표시 등)인 로직은 `query` 패키지에서 **MyBatis**를 사용합니다.
    *   복잡한 조인, 통계, DTO 매핑 최적화에 유리합니다.

### 1-2. WebSocket Convention
*   **Prefix:** 클라이언트 요청은 `/app`, 서버 브로드캐스팅은 `/topic`을 사용합니다.
*   **Payload:** 모든 실시간 메시지는 JSON 포맷으로 주고받습니다.

### 1-3. Security & Utils
*   `SecurityUtil.getCurrentUserId()`를 통해 언제든 안전하게 현재 사용자 ID를 획득할 수 있습니다.
*   하드코딩된 ID 대신 반드시 동적 ID를 사용하세요.

---

## 2. 🚀 Getting Started

### 1-1. 실행 환경
*   **JDK 17 +**
*   **MariaDB 10.6 +**
*   **Redis** (Optional, if configured)

### 1-2. 실행 방법
```bash
./gradlew bootRun
```
서버가 정상적으로 실행되면 `http://localhost:8080`으로 접속하여 테스트할 수 있습니다.

---

## 2. 🔌 API Testing Guide

개발 중인 API를 테스트하는 방법은 크게 두 가지가 있습니다.

### 2-1. Swagger UI 활용 (추천 ✅)
브라우저에서 API 명세를 확인하고 즉시 요청을 보낼 수 있습니다.
*   **URL:** [`http://localhost:8080/swagger-ui/index.html`](http://localhost:8080/swagger-ui/index.html)
*   **기능:** API Endpoint 확인, Try it out 기능을 통한 요청/응답 테스트.

### 2-2. IntelliJ HTTP Client 활용
`http` 디렉토리 내에 있는 `.http` 파일을 통해 시나리오 테스트를 수행할 수 있습니다.
*   **파일 위치:** `next-page/http/api-test.http`
*   **사용법:**
    1. IntelliJ에서 해당 파일을 엽니다.
    2. `Run` 버튼(▶️)을 클릭하여 요청을 전송합니다.
    3. `global.json` 또는 환경 변수를 통해 JWT 토큰을 관리하며 연속적인 시나리오 테스트가 가능합니다.

---

## 3. 🏗️ Module-Specific Guide (담당자별 가이드)

### 👤 Member & Auth (김태형)
*   **보안 설정 (`SecurityConfig`):** URL 별 권한 설정이 필요할 때 수정하세요.
*   **JWT:** `JwtTokenProvider`에서 토큰 생성/검증 로직을 관리합니다.
*   **Soft Delete:** 회원 탈퇴 시 `DELETE` 쿼리 대신 `status = 'DELETED'` 업데이트를 수행합니다.
*   **트랜잭션:** 회원 상태 변경 등 중요 로직에는 반드시 `@Transactional`을 적용하세요.

### 📖 Book & Writing (정진호)
*   **동시성 제어:** 여러 명이 동시에 문장을 입력할 때의 충돌 방지가 핵심입니다.
    *   `last_writer_user_id`를 체크하여 연속 작성을 방지합니다 (`Book` 엔티티 참조).
    *   `SEQUENCE_MISMATCH` 예외를 통해 오직 **마지막 문장**만 수정/삭제할 수 있도록 제한합니다. (과거 기록 보호)
*   **편집 잠금 (Locking):** 문장을 편집하는 동안 `TypingController`를 통해 수정 중임을 알리고 다른 유저의 입력을 차단합니다.
*   **WebSocket:** `TypingController`에서 실시간 입력 상태를 브로드캐스팅합니다.
    *   Topic: `/topic/typing/{bookId}` (Book ID별 격리 필수)

### ❤️ Reaction & Comment (정병진)
*   **실시간 댓글 (Real-time):** 댓글 등록 시 `ReactionController`에서 `SimpMessagingTemplate`를 사용해 `/topic/comments/{bookId}`로 이벤트를 발행합니다.
    *   프론트엔드는 이 토픽을 구독하여 새로고침 없이 댓글을 즉시 리스트에 추가합니다.
*   **계층형 댓글:** `parent_id`를 활용한 자기참조 구조입니다.
    *   조회 시 계층 구조로 변환하는 로직(`ReactionQueryService`)을 주의 깊게 다루세요.
*   **투표 (Vote):** 1인 1투표 원칙을 `uk_book_voter`, `uk_sentence_voter` 유니크 키로 보장합니다.

---

## 4. 🌐 Real-time System Flow

### 4-1. WebSocket & STOMP Flow
본 프로젝트는 **Spring WebSocket + STOMP**를 사용하여 실시간 양방향 통신을 구현합니다.

1.  **Frontend Connect:** `new SockJS('/ws')` -> `Stomp.over(socket)` -> `.connect()`
2.  **Subscribe:**
    *   `/topic/typing/{bookId}`: 타이핑 상태 감지 (누가 쓰고 있는지)
    *   `/topic/comments/{bookId}`: 새 댓글 감지
    *   `/topic/sentences/{bookId}`: 새 문장(이어쓰기) 감지
3.  **Publish (Frontend):** 타이핑 시작/종료 시 `/app/typing` 등으로 메시지 전송.
4.  **Publish (Backend Events):** 댓글/문장 작성 완료 시 컨트롤러에서 `convertAndSend`로 이벤트 브로드캐스팅.

---

## 5. 🚀 MSA(Microservices Architecture) 전환 상세 가이드

이 섹션은 현재의 Monolithic 아키텍처를 MSA 환경으로 안전하고 완전하게 전환하기 위한 **단계별 통합 매뉴얼**입니다. 도메인 분리와 서비스 간 통신 설정을 포함하여 100% 전환이 가능하도록 가이드합니다.

### 5-1. 목표 아키텍처 (Target Architecture)

```text
                  [Service Discovery: Eureka Server]
                           |  (Port 8761)
                           |
            [Gateway Server: Spring Cloud Gateway]
                    |  (Port 8000)
                    |  - JWT 검증
                    |  - 라우팅
                    |  - 헤더 주입 (X-User-Id, X-User-Email, etc.)
                    |
    ----------------------------------------------------------------
    |                    |                       |
[Member Service]   [Story Service]      [Reaction Service]
(Auth/User)        (Book/Sentence)      (Comment/Vote)
Port: 8081         Port: 8082           Port: 8083
    |                    |                       |
[DB: Member]       [DB: Story]          [DB: Reaction]
next_page_member   next_page_story      next_page_reaction
```

**서비스 간 통신:**
- **동기 통신**: OpenFeign (REST API)
- **비동기 통신**: Kafka/RabbitMQ (이벤트 기반, 선택적)

### 5-2. 사전 준비 (Workspace Setup)

#### Step 1: 프로젝트 구조 생성
```bash
# 현재 구조
team2/
├── next-page/           # Monolithic 프로젝트 (기존)
└── next-page-msa/       # MSA 프로젝트 (신규)
```

#### Step 2: Gradle Multi-Module 설정
`next-page-msa/settings.gradle`:
```gradle
rootProject.name = 'next-page-msa'

include 'common-module'
include 'discovery-server'
include 'gateway-server'
include 'config-server'
include 'member-service'
include 'story-service'
include 'reaction-service'
```

---

### 5-3. 데이터베이스 분리 작업 (최우선)

#### Step 1: 데이터베이스 생성 스크립트 실행
```bash
# 위치: next-page-msa/database-scripts/
cd next-page-msa/database-scripts

# MariaDB 로그인
mysql -u root -p

# 스크립트 실행
source 01-create-databases.sql
source 02-member-service-schema.sql
source 03-story-service-schema.sql
source 04-reaction-service-schema.sql
```

#### Step 2: 생성된 데이터베이스 확인
```sql
-- 3개의 DB 확인
SHOW DATABASES LIKE 'next_page_%';
-- Expected: next_page_member, next_page_story, next_page_reaction

-- Member DB 테이블 확인
USE next_page_member;
SHOW TABLES;  -- users, refresh_tokens

-- Story DB 테이블 확인
USE next_page_story;
SHOW TABLES;  -- categories, books, sentences

-- Reaction DB 테이블 확인
USE next_page_reaction;
SHOW TABLES;  -- comments, book_votes, sentence_votes
```

#### Step 3: 데이터베이스 설계 특징

**1. Member Service DB (next_page_member)**
```sql
-- 사용자 테이블
users (
  user_id INT PK,
  user_email VARCHAR(100) UK,
  user_nicknm VARCHAR(50) UK,
  user_role VARCHAR(20),        -- USER, ADMIN
  user_status VARCHAR(20),      -- ACTIVE, DELETED
  left_at DATETIME,             -- Soft Delete
  ...
)

-- JWT 리프레시 토큰 (Redis 대안)
refresh_tokens (
  token_id BIGINT PK,
  user_id INT,
  refresh_token VARCHAR(500) UK,
  expires_at DATETIME,
  ...
)
```

**2. Story Service DB (next_page_story)**
```sql
-- 카테고리
categories (
  category_id VARCHAR(20) PK,   -- THRILLER, ROMANCE, etc.
  category_nm VARCHAR(50)
)

-- 소설 (Aggregate Root)
books (
  book_id INT PK,
  writer_id INT,                -- Member Service의 user_id (외래키 X)
  category_id VARCHAR(20) FK,
  title VARCHAR(200),
  status VARCHAR(20),           -- WRITING, COMPLETED
  current_sequence INT,
  max_sequence INT,
  last_writer_user_id INT,      -- 연속 작성 방지
  ...
)

-- 문장 (Aggregate 소속)
sentences (
  sentence_id INT PK,
  book_id INT FK,
  writer_id INT,                -- Member Service의 user_id
  content TEXT,
  sequence_no INT,
  UK (book_id, sequence_no)     -- 복합 유니크
  ...
)
```

**3. Reaction Service DB (next_page_reaction)**
```sql
-- 댓글 (계층형 구조)
comments (
  comment_id INT PK,
  parent_id INT FK,             -- 대댓글 부모
  book_id INT,                  -- Story Service의 book_id
  writer_id INT,                -- Member Service의 user_id
  content TEXT,
  deleted_at DATETIME,          -- Soft Delete
  ...
)

-- 소설 투표 (1인 1투표)
book_votes (
  vote_id INT PK,
  book_id INT,
  voter_id INT,
  vote_type VARCHAR(10),        -- LIKE, DISLIKE
  UK (book_id, voter_id)        -- 1인 1투표 제약
)

-- 문장 투표
sentence_votes (
  vote_id INT PK,
  sentence_id INT,
  voter_id INT,
  vote_type VARCHAR(10),
  UK (sentence_id, voter_id)
)
```

#### Step 4: 데이터베이스 접근 권한 설정
```sql
-- 서비스별 전용 계정 (운영 환경에서 필수)
CREATE USER 'member_service'@'%' IDENTIFIED BY 'secure_password_1';
GRANT ALL PRIVILEGES ON next_page_member.* TO 'member_service'@'%';

CREATE USER 'story_service'@'%' IDENTIFIED BY 'secure_password_2';
GRANT ALL PRIVILEGES ON next_page_story.* TO 'story_service'@'%';

CREATE USER 'reaction_service'@'%' IDENTIFIED BY 'secure_password_3';
GRANT ALL PRIVILEGES ON next_page_reaction.* TO 'reaction_service'@'%';

FLUSH PRIVILEGES;
```

**상세 가이드:** [database-scripts/README.md](next-page-msa/database-scripts/README.md)

---

### 5-4. [Module 1] `common-module` (공통 라이브러리)

모든 서비스가 의존하는 핵심 유틸리티 모듈입니다. 다른 서비스보다 **가장 먼저** 구축해야 합니다.

#### 위치 및 구조
```
next-page-msa/common-module/
└── src/main/java/com/team2/commonmodule/
    ├── entity/
    │   └── BaseEntity.java                  (JPA Auditing)
    ├── error/
    │   ├── BusinessException.java           (커스텀 예외)
    │   └── ErrorCode.java                   (에러 코드 Enum)
    ├── response/
    │   └── ApiResponse.java                 (표준 응답 포맷)
    ├── exception/
    │   └── GlobalExceptionHandler.java      (전역 예외 처리)
    ├── util/
    │   └── SecurityUtil.java                (사용자 정보 조회)
    └── config/
        └── ModelMapperConfig.java           (DTO 변환)
```

#### 이동 대상 코드 (From Monolithic)
```
next-page/src/main/java/com/team2/nextpage/
├── common/**                        → common-module/
│   ├── entity/BaseEntity.java
│   ├── error/BusinessException.java
│   ├── error/ErrorCode.java
│   ├── response/ApiResponse.java
│   ├── exception/GlobalExceptionHandler.java
│   └── util/SecurityUtil.java
└── config/ModelMapperConfig.java    → common-module/config/
```

#### MSA 환경을 위한 주요 수정 사항

**1. SecurityUtil.java (중요!)**
- Monolithic: Spring Security Context에서 `CustomUserDetails` 조회
- MSA: Gateway가 주입한 **HTTP 헤더**에서 사용자 정보 추출

```java
// MSA 버전 (Gateway 헤더 기반)
public class SecurityUtil {
    private static final String HEADER_USER_ID = "X-User-Id";
    private static final String HEADER_USER_EMAIL = "X-User-Email";
    private static final String HEADER_USER_NICKNAME = "X-User-Nickname";
    private static final String HEADER_USER_ROLE = "X-User-Role";

    public static Long getCurrentUserId() {
        HttpServletRequest request = getCurrentRequest();
        String userIdHeader = request.getHeader(HEADER_USER_ID);
        return Long.parseLong(userIdHeader);
    }
    // ... 기타 메서드
}
```

**2. ErrorCode.java**
- 기존 에러 코드 유지
- MSA별 신규 에러 추가:
  - `INVALID_TOKEN` (A004)
  - `BOOK_NOT_FOUND` (B006)
  - `ALREADY_VOTED` (R005)

**3. ApiResponse.java**
- 메서드명 수정: `listError()` → `error()`

#### build.gradle 설정
```gradle
plugins {
    id 'java-library'
}

dependencies {
    // Spring Boot Starters
    api 'org.springframework.boot:spring-boot-starter-web'
    api 'org.springframework.boot:spring-boot-starter-validation'
    api 'org.springframework.boot:spring-boot-starter-data-jpa'

    // ModelMapper
    api 'org.modelmapper:modelmapper:3.2.0'

    // Jackson
    api 'com.fasterxml.jackson.core:jackson-databind'

    // Lombok
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'
}
```

#### 각 서비스에서 사용하는 방법
```gradle
// member-service/build.gradle
dependencies {
    implementation project(':common-module')
    // ...
}
```

---

### 5-5. [Module 2] `discovery-server` (Service Discovery)

#### 역할
- 모든 마이크로서비스를 등록하고 관리
- Gateway가 서비스 위치를 동적으로 조회
- 서비스 헬스체크 및 로드밸런싱

#### application.yml
```yaml
server:
  port: 8761

spring:
  application:
    name: discovery-server

eureka:
  instance:
    hostname: localhost
  client:
    register-with-eureka: false  # 서버 자신은 등록 안 함
    fetch-registry: false
    service-url:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
```

#### Main Class
```java
@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DiscoveryServerApplication.class, args);
    }
}
```

#### 접속 확인
- URL: http://localhost:8761
- 등록된 서비스 목록 확인 가능

---

### 5-6. [Module 3] `gateway-server` (API Gateway & Security)

#### 역할
1. **라우팅**: 클라이언트 요청을 적절한 마이크로서비스로 전달
2. **JWT 검증**: Gateway에서 토큰을 한 번만 검증 (성능 최적화)
3. **헤더 주입**: 검증된 사용자 정보를 헤더로 내부 서비스에 전달
4. **CORS 처리**: 프론트엔드 연동을 위한 CORS 설정

#### application.yml (완전한 설정)
```yaml
server:
  port: 8000

spring:
  application:
    name: gateway-server

  cloud:
    gateway:
      # CORS 설정
      globalcors:
        cors-configurations:
          '[/**]':
            allowed-origins:
              - "http://localhost:3000"
              - "http://localhost:5173"
              - "http://localhost:8080"
            allowed-methods:
              - GET
              - POST
              - PUT
              - PATCH
              - DELETE
              - OPTIONS
            allowed-headers: "*"
            allow-credentials: true
            max-age: 3600

      # 라우팅 규칙
      routes:
        # Member Service
        - id: member-service
          uri: lb://MEMBER-SERVICE  # Eureka에서 조회
          predicates:
            - Path=/api/auth/**, /api/members/**
          filters:
            - RewritePath=/api/(?<segment>.*), /$\{segment}

        # Story Service
        - id: story-service
          uri: lb://STORY-SERVICE
          predicates:
            - Path=/api/books/**, /api/categories/**, /ws/**
          filters:
            - RewritePath=/api/(?<segment>.*), /$\{segment}

        # Reaction Service
        - id: reaction-service
          uri: lb://REACTION-SERVICE
          predicates:
            - Path=/api/reactions/**
          filters:
            - RewritePath=/api/(?<segment>.*), /$\{segment}

# Eureka 클라이언트 설정
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
    register-with-eureka: true
    fetch-registry: true
  instance:
    prefer-ip-address: true

# JWT Secret (member-service와 동일해야 함)
jwt:
  secret: ${JWT_SECRET:0b9e53ea3228c51635b0ee816888ba580e00dcd961d0d9c976a2f40dcf57bcfd}
```

#### JWT 필터 구현 (핵심 코드)

**1. JwtAuthenticationFilter.java**
```java
@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    @Value("${jwt.secret}")
    private String secretKey;

    private static final List<String> EXCLUDED_PATHS = List.of(
        "/api/auth/login",
        "/api/auth/signup",
        "/api/auth/refresh"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // 인증 제외 경로
        if (EXCLUDED_PATHS.stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }

        // Authorization 헤더 추출
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return onError(exchange, "Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);

        try {
            // JWT 검증
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();

            // 사용자 정보 추출
            String userId = claims.getSubject();
            String email = claims.get("email", String.class);
            String nickname = claims.get("nickname", String.class);
            String role = claims.get("role", String.class);

            // 헤더에 사용자 정보 주입
            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header("X-User-Id", userId)
                .header("X-User-Email", email)
                .header("X-User-Nickname", nickname)
                .header("X-User-Role", role)
                .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());

        } catch (JwtException e) {
            return onError(exchange, "Invalid JWT token", HttpStatus.UNAUTHORIZED);
        }
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String errorResponse = String.format(
            "{\"success\":false,\"code\":\"UNAUTHORIZED\",\"message\":\"%s\"}",
            message
        );

        DataBuffer buffer = response.bufferFactory().wrap(errorResponse.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return -100; // 최우선 필터
    }
}
```

**2. build.gradle**
```gradle
dependencies {
    // Spring Cloud Gateway
    implementation 'org.springframework.cloud:spring-cloud-starter-gateway'

    // Eureka Client
    implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'

    // JWT
    implementation 'io.jsonwebtoken:jjwt-api:0.12.6'
    runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.12.6'
    runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.12.6'

    // Reactive Support
    implementation 'org.springframework.boot:spring-boot-starter-webflux'
}
```

#### 테스트 방법
```bash
# 1. 로그인하여 JWT 토큰 획득
curl -X POST http://localhost:8000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user1@nextpage.com","password":"password123"}'

# 응답: { "accessToken": "eyJhbGc...", ... }

# 2. 토큰으로 보호된 API 호출
curl -X GET http://localhost:8000/api/books \
  -H "Authorization: Bearer eyJhbGc..."

# 3. Gateway에서 member-service로 라우팅됨
# 4. member-service는 X-User-Id 헤더로 사용자 식별
```

---

### 5-7. [Domain Services] 마이크로서비스별 이관 가이드

#### 공통 원칙
1. **패키지 구조 유지**: `com.team2.nextpage.*` → 그대로 유지 (수정 최소화)
2. **객체 참조 제거**: Entity 간 JPA 연관관계 제거 → ID 참조로 변경
3. **Feign Client 추가**: 타 서비스 데이터 조회용
4. **common-module 의존**: 공통 코드는 모듈로 분리

---

#### 📦 [member-service] (Port: 8081)

**담당자:** 김태형
**데이터베이스:** `next_page_member`

**이관 대상:**
```
next-page/src/main/java/com/team2/nextpage/
├── auth/**                          → member-service/
│   ├── controller/AuthController
│   ├── service/AuthService
│   ├── repository/AuthRepository
│   └── dto/*
│
├── command/member/**                → member-service/
│   ├── controller/MemberController
│   ├── service/MemberService
│   ├── repository/MemberRepository
│   ├── entity/Member.java
│   ├── entity/UserRole.java
│   ├── entity/UserStatus.java
│   └── dto/**
│
├── query/member/**                  → member-service/
│   ├── controller/MemberQueryController
│   ├── service/MemberQueryService
│   └── dto/**
│
├── config/security/**               → member-service/config/
│   ├── SecurityConfig.java
│   ├── CustomUserDetails.java
│   └── CustomUserDetailsService.java
│
└── jwt/**                           → member-service/jwt/
    ├── JwtTokenProvider.java
    └── dto/JwtTokenResponse.java

next-page/src/main/resources/
└── mapper/member/*.xml              → member-service/resources/mapper/
```

**application.yml:**
```yaml
server:
  port: 8081

spring:
  application:
    name: MEMBER-SERVICE
  datasource:
    url: jdbc:mariadb://localhost:3306/next_page_member
    username: member_service
    password: member_pw_2026
  jpa:
    hibernate:
      ddl-auto: validate  # 운영: validate, 개발: update

mybatis:
  mapper-locations: classpath:mapper/member/**/*.xml

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/

jwt:
  secret: ${JWT_SECRET}
  access-token-validity-in-seconds: 3600
  refresh-token-validity-in-seconds: 604800
```

**build.gradle:**
```gradle
dependencies {
    implementation project(':common-module')

    // Spring Boot Starters
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-security'

    // MyBatis
    implementation 'org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.5'

    // JWT
    implementation 'io.jsonwebtoken:jjwt-api:0.12.6'
    runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.12.6'
    runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.12.6'

    // Eureka Client
    implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'

    // Database
    runtimeOnly 'org.mariadb.jdbc:mariadb-java-client'

    // Swagger
    implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5'
}
```

**추가 작업:**
- `SecurityConfig`: Gateway 검증 후 내부망이므로 인증 로직 단순화
- `CustomUserDetailsService`: 기존 유지

---

#### 📦 [story-service] (Port: 8082)

**담당자:** 정진호
**데이터베이스:** `next_page_story`

**이관 대상:**
```
next-page/src/main/java/com/team2/nextpage/
├── command/book/**                  → story-service/
│   ├── controller/BookController
│   ├── service/BookService
│   ├── repository/BookRepository
│   ├── repository/SentenceRepository
│   ├── entity/Book.java             (수정 필요)
│   ├── entity/Sentence.java         (수정 필요)
│   ├── entity/BookStatus.java
│   └── dto/**
│
├── query/book/**                    → story-service/
│   ├── controller/BookQueryController
│   ├── service/BookQueryService
│   ├── mapper/BookMapper.java
│   └── dto/**
│
├── category/**                      → story-service/
│   ├── controller/CategoryController
│   ├── repository/CategoryRepository
│   ├── entity/Category.java
│   └── dto/**
│
├── websocket/**                     → story-service/
│   ├── controller/TypingController
│   └── dto/**
│
└── config/WebSocketConfig.java      → story-service/config/

next-page/src/main/resources/
├── mapper/book/*.xml                → story-service/resources/mapper/
└── mapper/category/*.xml            → story-service/resources/mapper/
```

**Entity 수정 (중요!):**
```java
// Before (Monolithic)
@Entity
public class Book {
    @ManyToOne
    @JoinColumn(name = "writer_id")
    private Member writer;  // ❌ 객체 참조
}

// After (MSA)
@Entity
public class Book {
    @Column(name = "writer_id")
    private Long writerId;  // ✅ ID 참조

    // Feign Client로 작성자 정보 조회
    // @Transient
    // private MemberDto writerInfo;
}
```

**Feign Client 추가:**
```java
@FeignClient(name = "MEMBER-SERVICE")
public interface MemberServiceClient {
    @GetMapping("/members/{memberId}")
    ApiResponse<MemberDto> getMember(@PathVariable Long memberId);

    @GetMapping("/members/batch")
    ApiResponse<List<MemberDto>> getMembersBatch(@RequestParam List<Long> memberIds);
}
```

**application.yml:**
```yaml
server:
  port: 8082

spring:
  application:
    name: STORY-SERVICE
  datasource:
    url: jdbc:mariadb://localhost:3306/next_page_story
    username: story_service
    password: story_pw_2026

mybatis:
  mapper-locations: classpath:mapper/book/**/*.xml, classpath:mapper/category/**/*.xml

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/

# WebSocket은 Gateway를 통해서도 접근 가능
```

---

#### 📦 [reaction-service] (Port: 8083)

**담당자:** 정병진
**데이터베이스:** `next_page_reaction`

**이관 대상:**
```
next-page/src/main/java/com/team2/nextpage/
├── command/reaction/**              → reaction-service/
│   ├── controller/ReactionController
│   ├── service/ReactionService
│   ├── repository/CommentRepository
│   ├── repository/BookVoteRepository
│   ├── repository/SentenceVoteRepository
│   ├── entity/Comment.java          (수정 필요)
│   ├── entity/BookVote.java         (수정 필요)
│   ├── entity/SentenceVote.java     (수정 필요)
│   ├── entity/VoteType.java
│   └── dto/**
│
└── query/reaction/**                → reaction-service/
    ├── controller/ReactionQueryController
    ├── service/ReactionQueryService
    ├── mapper/ReactionMapper.java
    └── dto/**

next-page/src/main/resources/
└── mapper/reaction/*.xml            → reaction-service/resources/mapper/
```

**Entity 수정:**
```java
// Comment.java
@Entity
public class Comment {
    @Column(name = "book_id")
    private Long bookId;      // Story Service의 book_id

    @Column(name = "writer_id")
    private Long writerId;    // Member Service의 user_id

    // 대댓글은 같은 DB 내에 있으므로 JPA 관계 유지 가능
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parent;
}
```

**Feign Client 추가:**
```java
@FeignClient(name = "STORY-SERVICE")
public interface StoryServiceClient {
    @GetMapping("/books/{bookId}")
    ApiResponse<BookDto> getBook(@PathVariable Long bookId);

    @GetMapping("/sentences/{sentenceId}")
    ApiResponse<SentenceDto> getSentence(@PathVariable Long sentenceId);
}

@FeignClient(name = "MEMBER-SERVICE")
public interface MemberServiceClient {
    @GetMapping("/members/{memberId}")
    ApiResponse<MemberDto> getMember(@PathVariable Long memberId);
}
```

**application.yml:**
```yaml
server:
  port: 8083

spring:
  application:
    name: REACTION-SERVICE
  datasource:
    url: jdbc:mariadb://localhost:3306/next_page_reaction
    username: reaction_service
    password: reaction_pw_2026

mybatis:
  mapper-locations: classpath:mapper/reaction/**/*.xml

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

---

### 5-8. MSA 전환 시 주요 고려사항

#### 1. 데이터베이스 조인 처리
**문제:** 서로 다른 DB의 테이블을 JOIN할 수 없음

**해결 방법:**
```java
// ❌ Before (Monolithic - MyBatis)
SELECT
    b.*,
    u.user_nicknm AS writer_nickname
FROM books b
JOIN users u ON b.writer_id = u.user_id;

// ✅ After (MSA - Application Level Join)
@Service
public class BookQueryService {
    private final BookMapper bookMapper;
    private final MemberServiceClient memberClient;

    public BookDetailDto getBookDetail(Long bookId) {
        // 1. Story Service에서 소설 조회
        BookDto book = bookMapper.findBookById(bookId);

        // 2. Member Service에서 작성자 정보 조회
        MemberDto writer = memberClient.getMember(book.getWriterId()).getData();

        // 3. DTO 병합
        return BookDetailDto.builder()
            .bookId(book.getBookId())
            .title(book.getTitle())
            .writerNickname(writer.getNickname())
            .build();
    }
}
```

**성능 최적화:**
- **배치 조회**: N+1 문제 방지
```java
// 소설 목록 조회 시 작성자 정보를 배치로 조회
List<BookDto> books = bookMapper.findBooks();
List<Long> writerIds = books.stream().map(BookDto::getWriterId).distinct().collect(Collectors.toList());
Map<Long, MemberDto> writerMap = memberClient.getMembersBatch(writerIds).getData()
    .stream().collect(Collectors.toMap(MemberDto::getUserId, Function.identity()));

// 병합
books.forEach(book -> book.setWriterNickname(writerMap.get(book.getWriterId()).getNickname()));
```

#### 2. 보안 컨텍스트 변경
**Monolithic:** Spring Security Context
**MSA:** Gateway HTTP 헤더

```java
// Monolithic SecurityUtil
public static Long getCurrentUserId() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
    return user.getUserId();
}

// MSA SecurityUtil (common-module)
public static Long getCurrentUserId() {
    HttpServletRequest request = getCurrentRequest();
    return Long.parseLong(request.getHeader("X-User-Id"));
}
```

**각 서비스의 SecurityConfig 간소화:**
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        // Gateway에서 이미 검증했으므로 모든 요청 허용
        return http.build();
    }
}
```

#### 3. 트랜잭션 관리 (분산 트랜잭션)

**문제:** 서비스 간 트랜잭션 원자성 보장 불가

**해결 방법:**

**Option 1: SAGA 패턴 (Choreography)**
```java
// 예: 댓글 작성 시 알림 전송
@Service
public class CommentService {
    @Transactional
    public void createComment(CreateCommentRequest request) {
        // 1. 댓글 저장
        Comment comment = commentRepository.save(...);

        // 2. 이벤트 발행 (Kafka)
        kafkaTemplate.send("comment-created", CommentCreatedEvent.builder()
            .commentId(comment.getCommentId())
            .bookId(comment.getBookId())
            .writerId(comment.getWriterId())
            .build());
    }
}

// Member Service에서 이벤트 수신 후 알림 전송
@KafkaListener(topics = "comment-created")
public void handleCommentCreated(CommentCreatedEvent event) {
    // 알림 전송 로직
}
```

**Option 2: 보상 트랜잭션 (Compensation)**
```java
@Service
public class BookService {
    public void createBook(CreateBookRequest request) {
        try {
            // 1. 소설 생성
            Book book = bookRepository.save(...);

            // 2. Member Service에 작가 검증 요청
            memberClient.validateMember(request.getWriterId());

        } catch (FeignException e) {
            // Member Service 실패 시 보상: 생성한 소설 삭제
            bookRepository.deleteById(book.getBookId());
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
        }
    }
}
```

**Option 3: 엄격한 사전 검증 (Recommended for MVP)**
```java
@Service
public class CommentService {
    public void createComment(CreateCommentRequest request) {
        // 1. 사전 검증: Book 존재 여부
        BookDto book = storyClient.getBook(request.getBookId()).getData();
        if (book == null) {
            throw new BusinessException(ErrorCode.BOOK_NOT_FOUND);
        }

        // 2. 댓글 저장 (단일 DB 트랜잭션)
        commentRepository.save(...);
    }
}
```

#### 4. WebSocket 통신 (Story Service)

**MSA 환경에서의 WebSocket 고려사항:**
- Gateway를 통한 WebSocket 프록시 설정
- STOMP 메시지 브로커 외부화 (RabbitMQ/Redis Pub/Sub)

**Gateway WebSocket 설정:**
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: story-websocket
          uri: lb:ws://STORY-SERVICE
          predicates:
            - Path=/ws/**
```

---

### 5-9. MSA 실행 순서 및 테스트

#### 실행 순서
```bash
# 1. Discovery Server
cd next-page-msa/discovery-server
./gradlew bootRun

# 2. Gateway Server
cd next-page-msa/gateway-server
./gradlew bootRun

# 3. 도메인 서비스 (병렬 실행 가능)
cd next-page-msa/member-service && ./gradlew bootRun &
cd next-page-msa/story-service && ./gradlew bootRun &
cd next-page-msa/reaction-service && ./gradlew bootRun &
```

#### 서비스 확인
| 서비스 | URL | 상태 확인 |
|--------|-----|----------|
| Eureka Dashboard | http://localhost:8761 | 등록된 서비스 목록 |
| Gateway | http://localhost:8000 | 라우팅 테스트 |
| Member API | http://localhost:8081/swagger-ui.html | Swagger |
| Story API | http://localhost:8082/swagger-ui.html | Swagger |
| Reaction API | http://localhost:8083/swagger-ui.html | Swagger |

#### E2E 테스트 시나리오
```bash
# 1. 회원가입 (Member Service via Gateway)
curl -X POST http://localhost:8000/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "nickname": "테스터"
  }'

# 2. 로그인
curl -X POST http://localhost:8000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
# 응답에서 accessToken 복사

# 3. 소설 생성 (Story Service via Gateway)
curl -X POST http://localhost:8000/api/books \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "테스트 소설",
    "categoryId": "THRILLER",
    "maxSequence": 10
  }'

# 4. 소설 목록 조회 (작성자 닉네임 포함 - Feign Client)
curl -X GET "http://localhost:8000/api/books" \
  -H "Authorization: Bearer <token>"

# 5. 댓글 작성 (Reaction Service via Gateway)
curl -X POST http://localhost:8000/api/reactions/comments \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "bookId": 1,
    "content": "재미있네요!"
  }'
```

---

### 5-10. MSA 전환 체크리스트

#### 인프라
- [ ] MariaDB 3개 데이터베이스 생성 (`next_page_member`, `next_page_story`, `next_page_reaction`)
- [ ] 데이터베이스 스키마 생성 (script 실행)
- [ ] Eureka Server 실행 및 접속 확인 (http://localhost:8761)
- [ ] Gateway Server 실행 및 라우팅 테스트

#### 공통 모듈
- [ ] common-module 빌드 성공
- [ ] 각 서비스에서 common-module 의존성 추가
- [ ] SecurityUtil MSA 버전 적용 (헤더 기반)

#### 서비스별
**Member Service:**
- [ ] Entity/Repository/Service/Controller 이관
- [ ] JWT 토큰 발급 로직 동작 확인
- [ ] Eureka 등록 확인
- [ ] Swagger 접속 (http://localhost:8081/swagger-ui.html)

**Story Service:**
- [ ] Entity ID 참조로 변경 (Member 객체 → writerId)
- [ ] Feign Client 구현 (MemberServiceClient)
- [ ] WebSocket 설정 이관
- [ ] Eureka 등록 확인

**Reaction Service:**
- [ ] Entity ID 참조로 변경
- [ ] Feign Client 구현 (MemberServiceClient, StoryServiceClient)
- [ ] 실시간 댓글 브로드캐스팅 테스트

#### 통합 테스트
- [ ] Gateway를 통한 로그인 성공
- [ ] Gateway → Member Service 라우팅
- [ ] Gateway → Story Service 라우팅
- [ ] Gateway → Reaction Service 라우팅
- [ ] Feign Client 통신 정상 (Application Level Join)
- [ ] WebSocket 연결 (Gateway 프록시)

---

### 5-11. Troubleshooting

#### 1. Eureka 등록 안 됨
```yaml
# application.yml 확인
eureka:
  client:
    register-with-eureka: true  # false면 등록 안 됨
    fetch-registry: true
```

#### 2. Feign Client 호출 실패
```
FeignException$ServiceUnavailable: [503] Service Unavailable
```
**원인:** 대상 서비스가 Eureka에 등록되지 않음
**해결:** 대상 서비스 실행 확인, Eureka 등록 확인

#### 3. Gateway JWT 검증 실패
```
Invalid JWT token
```
**원인:** Gateway와 Member Service의 JWT Secret 불일치
**해결:** 두 서비스 모두 동일한 `jwt.secret` 값 사용

#### 4. CORS 에러
```
Access to fetch at 'http://localhost:8000/api/books' from origin 'http://localhost:3000' has been blocked by CORS policy
```
**원인:** Gateway CORS 설정 누락
**해결:** Gateway `application.yml`에 `globalcors` 설정 추가

---

## 6. 🧪 Testing Strategy

### 6-1. Unit Test (단위 테스트)
*   **Domain Logic:** 비즈니스 로직(엔티티 메서드 등)은 반드시 단위 테스트를 작성합니다.
*   **Service Layer:** Mockito를 활용하여 의존성을 격리하고 테스트하세요.

### 6-2. Integration Test (통합 테스트)
*   DB 연동이 필요한 쿼리나 전체 흐름 테스트는 `@SpringBootTest`를 활용합니다.
*   실제 `http/api-test.http`를 사용하여 End-to-End 시나리오를 점검하는 것을 권장합니다.

---

## 7. ⚠️ Troubleshooting
*   **인코딩 문제:** `application.yml`의 인코딩 설정(`UTF-8`)을 확인하세요.
*   **WebSocket 연결 실패:** 클라이언트의 `SockJS` 버전과 서버 설정을 확인하고, Security Config에서 `/ws/**` 경로가 허용되어 있는지 체크하세요.

---
**Last Updated:** 2026-01-14
