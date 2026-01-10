# 🤖 Next Page Project AI Guidelines

이 문서는 AI 어시스턴트가 'Next Page' 프로젝트의 코드를 작성하거나 수정할 때 준수해야 할 핵심 원칙과 규칙을 정의합니다.

## 1. 🎯 프로젝트 정체성 및 아키텍처
*   **프로젝트명:** Next Page (릴레이 소설 창작 플랫폼)
*   **핵심 철학:** 도메인 주도 설계(DDD) + 명령/조회 책임 분리(CQRS)
*   **기술 스택:** Java 17, Spring Boot 3.5.9, MariaDB
*   **ORM 전략:**
    *   **Command (CUD):** JPA (Hibernate) 사용. 객체 지향적 로직 구현, Dirty Checking 활용.
    *   **Query (R):** MyBatis 사용. 복잡한 조회, 조인, DTO 직접 매핑 성능 최적화.

## 2. 🏛️ 코딩 컨벤션 및 패턴

### 2.1 Entity & Domain Logic (DDD)
*   **위치:** `command.{module}.entity` 패키지에 위치. (e.g. `command.book.entity.Book`)
*   **Lombok 어노테이션 규칙 (Strict):**
    *   `@Getter`: 필수
    *   `@Builder`: 객체 생성 시 생성자 대신 사용 필수
    *   `@NoArgsConstructor(access = AccessLevel.PROTECTED)`: JPA 필수
    *   `@AllArgsConstructor(access = AccessLevel.PRIVATE)`: Builder와 함께 사용
    *   `@Setter`: **사용 금지**. 상태 변경은 명확한 의도를 가진 메서드(Business Method)로 구현.
*   **도메인 로직 위치:** 비즈니스 규칙은 Service가 아닌 **Entity 내부**에 위치시킨다.
    *   Ex) `book.validateWritingPossible()`, `book.completeStory()`

### 2.2 Layered Architecture Rules
*   **Controller:**
    *   요청값 검증(`@Valid`), 응답 변환(Representation)만 담당.
    *   **Dependency Injection:** `@RequiredArgsConstructor` 사용 (생성자 주입).
    *   **Response Format:** `ResponseEntity<ApiResponse<T>>` 반환 필수.
*   **Service:**
    *   트랜잭션 관리(`@Transactional`) 및 도메인 객체 간의 협력 조율(Orchestration).
    *   **Dependency Injection:** `@RequiredArgsConstructor` 사용.
*   **DTO:**
    *   JPA Entity <-> DTO 변환은 `ModelMapper`나 생성자/Builder 패턴 사용.
    *   Request/Response DTO는 `record` 또는 `static class`로 정의 권장.

### 2.3 CQRS 구현 규칙
*   **Command (쓰기):**
    *   **Package:** `com.team2.nextpage.command.*`
    *   Repository: `JpaRepository` 상속.
*   **Query (읽기):**
    *   **Package:** `com.team2.nextpage.query.*`
    *   Mapper: `MyBatis` Mapper Interface 사용 (`@Mapper`).
    *   XML 위치: `resources/mapper/**/*.xml`.

### 2.4 공통 모듈 구조 (Common)
*   **패키지 경로:** `com.team2.nextpage.common`
    *   `entity`: `BaseEntity` (JPA Audit: created_at, updated_at)
    *   `response`: `ApiResponse` (공통 응답 포맷)
    *   `exception`: `GlobalExceptionHandler` (전역 예외 처리)
    *   `error`: `ErrorCode`, `BusinessException`

### 2.5 Security & JWT
*   **Dependency:** `jjwt-api`, `jjwt-impl`, `jjwt-jackson`
*   **Authentication:** `Bearer` Token 방식 사용.

## 3. 🛠️ 라이브러리 및 도구 활용
*   **Lombok:** `@Getter`, `@Builder`, `@RequiredArgsConstructor`, `@Slf4j` 적극 활용.
*   **Validation:** `jakarta.validation` (`@NotNull`, `@Size` 등) 활용.
*   **Data Type:** 상태값 등은 String 대신 **Enum 사용 권장** (`@Enumerated(EnumType.STRING)`).
*   **Soft Delete:** `@SQLDelete` 및 `@SQLRestriction` 어노테이션을 사용하여 자동화. (Java 코드는 `delete()` 호출하지만 DB는 `UPDATE` 실행).

## 4. 📝 주석 및 커밋 가이드
*   **JavaDoc:** 클래스 및 주요 메서드, 특히 **팀원 힌트**가 필요한 부분에 상세히 기술.
*   **Commit Message:** `type: subject` 형식 (feat, fix, refactor, docs, chore).

---
**⚠️ 주의:** 위 규칙을 위반하는 코드를 제안하지 마십시오. 특히 **Entity Setter 사용**, **Builder 미사용**, **잘못된 패키지 참조(domain X -> entity O)**는 엄격히 금지됩니다.
