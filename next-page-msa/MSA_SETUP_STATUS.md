# Next-Page MSA 전환 현황

## ✅ 완료된 작업

### 1. 데이터베이스 분리 ✅
- [x] 3개 데이터베이스 생성 스크립트
  - `01-create-databases.sql` - DB 생성 및 계정 설정
  - `02-member-service-schema.sql` - Member Service 스키마
  - `03-story-service-schema.sql` - Story Service 스키마
  - `04-reaction-service-schema.sql` - Reaction Service 스키마
- [x] 샘플 데이터 포함
- [x] 상세 README 작성

**위치:** `next-page-msa/database-scripts/`

### 2. common-module (공통 라이브러리) ✅
- [x] BaseEntity.java - JPA Auditing
- [x] ErrorCode.java - 에러 코드 (MSA 확장)
- [x] BusinessException.java - 커스텀 예외
- [x] ApiResponse.java - 표준 응답 포맷
- [x] GlobalExceptionHandler.java - 전역 예외 처리
- [x] SecurityUtil.java - **MSA 버전 (Gateway 헤더 기반)**
- [x] ModelMapperConfig.java - DTO 변환
- [x] build.gradle 설정

**핵심 변경:**
```java
// SecurityUtil: HTTP 헤더에서 사용자 정보 추출
public static Long getCurrentUserId() {
    return Long.parseLong(request.getHeader("X-User-Id"));
}
```

### 3. Gradle 멀티 모듈 설정 ✅
- [x] 루트 build.gradle 설정 (bootJar 비활성화)
- [x] 서브 모듈 플러그인 설정 수정
- [x] UTF-8 인코딩 설정
- [x] Spring Cloud 의존성 관리

### 4. 인프라 서비스 Main 클래스 ✅
- [x] DiscoveryServerApplication.java
- [x] GatewayServerApplication.java
- [x] ConfigServerApplication.java

### 5. DEVELOPER_GUIDE.md 대폭 업데이트 ✅
- [x] 목표 아키텍처 다이어그램
- [x] 데이터베이스 분리 가이드
- [x] common-module 이관 가이드
- [x] Gateway JWT 필터 완전한 구현 코드
- [x] 도메인 서비스별 이관 가이드
  - member-service 상세 가이드
  - story-service 상세 가이드 (Entity 수정, Feign Client)
  - reaction-service 상세 가이드
- [x] MSA 주요 고려사항
  - Application Level Join
  - 분산 트랜잭션 (SAGA, 보상, 사전 검증)
- [x] 실행 순서 및 E2E 테스트 시나리오
- [x] 전환 체크리스트
- [x] Troubleshooting

---

## 🚧 남은 작업 (팀에서 진행 필요)

### 1. 코드 이관 작업
- [ ] **member-service** 코드 이관 (김태형)
  - auth 패키지
  - command/member 패키지
  - query/member 패키지
  - SecurityConfig 수정 (Gateway 헤더 기반)
  - JWT 토큰 발급 로직

- [ ] **story-service** 코드 이관 (정진호)
  - command/book 패키지
  - query/book 패키지
  - category 패키지
  - websocket 패키지
  - **Entity 수정:** Member 객체 → writerId (ID 참조)
  - **Feign Client 구현:** MemberServiceClient

- [ ] **reaction-service** 코드 이관 (정병진)
  - command/reaction 패키지
  - query/reaction 패키지
  - **Entity 수정:** book_id, writer_id (ID 참조)
  - **Feign Client 구현:** MemberServiceClient, StoryServiceClient

### 2. Gateway JWT 필터 구현
- [ ] JwtAuthenticationFilter.java 작성
  - JWT 검증
  - 헤더 주입 (X-User-Id, X-User-Email, etc.)
- [ ] application.yml 라우팅 설정
- [ ] CORS 설정

### 3. 설정 파일 작성
각 서비스의 `src/main/resources/application.yml`:
- [ ] discovery-server (Port: 8761)
- [ ] gateway-server (Port: 8000)
- [ ] member-service (Port: 8081, DB: next_page_member)
- [ ] story-service (Port: 8082, DB: next_page_story)
- [ ] reaction-service (Port: 8083, DB: next_page_reaction)

**참고:** DEVELOPER_GUIDE.md에 완전한 설정 코드 포함됨

### 4. MyBatis 매퍼 이관
- [ ] member/MemberMapper.xml
- [ ] book/BookMapper.xml
- [ ] reaction/ReactionMapper.xml

**주의:** 서비스 간 JOIN 제거 필요 (Application Level Join으로 대체)

### 5. 통합 테스트
- [ ] 데이터베이스 스크립트 실행 및 검증
- [ ] 각 서비스 빌드 성공 확인
- [ ] Eureka 등록 확인 (http://localhost:8761)
- [ ] Gateway 라우팅 테스트
- [ ] Feign Client 통신 테스트
- [ ] E2E 시나리오 테스트

---

## 📝 주요 변경 사항

### 1. Entity 수정 패턴
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
}
```

### 2. Feign Client 패턴
```java
@FeignClient(name = "MEMBER-SERVICE")
public interface MemberServiceClient {
    @GetMapping("/members/{memberId}")
    ApiResponse<MemberDto> getMember(@PathVariable Long memberId);

    @GetMapping("/members/batch")
    ApiResponse<List<MemberDto>> getMembersBatch(@RequestParam List<Long> memberIds);
}
```

### 3. Application Level Join
```java
// Story Service에서 작성자 정보 병합
BookDto book = bookMapper.findBookById(bookId);
MemberDto writer = memberClient.getMember(book.getWriterId()).getData();

return BookDetailDto.builder()
    .bookId(book.getBookId())
    .title(book.getTitle())
    .writerNickname(writer.getNickname())
    .build();
```

---

## 🔧 Gradle 빌드 명령어

```bash
# 전체 빌드 (테스트 제외)
./gradlew clean build -x test

# 특정 모듈 빌드
./gradlew :member-service:build -x test

# 전체 실행 (병렬)
./gradlew bootRun --parallel
```

---

## 📚 참고 문서

- **상세 가이드:** [DEVELOPER_GUIDE.md](DEVELOPER_GUIDE.md#5--migration-to-msa-microservices-guide)
- **데이터베이스:** [database-scripts/README.md](database-scripts/README.md)
- **Monolithic 소스:** [../next-page/](../next-page/)

---

## 🆘 문제 해결

### Gradle 빌드 실패
```bash
# Gradle Daemon 재시작
./gradlew --stop
./gradlew clean build -x test
```

### Eureka 등록 안 됨
`application.yml` 확인:
```yaml
eureka:
  client:
    register-with-eureka: true  # false면 등록 안 됨
```

### Feign Client 호출 실패
- Eureka에서 대상 서비스 등록 확인
- 서비스명(name) 일치 확인 (대소문자 구분)

---

**Last Updated:** 2026-01-14
**Status:** 인프라 설정 완료, 도메인 서비스 코드 이관 대기
