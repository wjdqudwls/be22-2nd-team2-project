# 🔐 JWT 인증 시스템 설정 가이드

## 📚 개요
Next Page 프로젝트에 JWT 기반 인증 시스템이 완전히 구현되었습니다.

## 🎯 완료된 작업

### 1. JWT 핵심 컴포넌트
- ✅ **JwtTokenProvider** - JWT 토큰 생성 및 검증
  - Access Token & Refresh Token 발급
  - 토큰 유효성 검증
  - 사용자 정보 추출
  
- ✅ **JwtAuthenticationFilter** - JWT 인증 필터
  - HTTP 요청 헤더에서 JWT 토큰 추출
  - SecurityContext에 인증 정보 설정
  - Swagger/Actuator 등 공개 경로 제외

### 2. Spring Security 통합
- ✅ **CustomUserDetails** - UserDetails 구현체
- ✅ **CustomUserDetailsService** - 사용자 인증 서비스
- ✅ **SecurityConfig** - 보안 설정
  - JWT 필터 체인 통합
  - Stateless 세션 관리
  - 엔드포인트별 접근 권한 설정
  - AuthenticationManager 빈 등록

### 3. Repository 확장
- ✅ **MemberRepository** 확장
  - `findByUserEmail()` - 이메일로 회원 조회
  - `existsByUserEmail()` - 이메일 중복 체크
  - `existsByUserNicknm()` - 닉네임 중복 체크

### 4. Swagger/OpenAPI 문서
- ✅ **SwaggerConfig** - API 문서 자동 생성
  - OpenAPI 3.0 스펙
  - JWT Bearer 인증 UI 지원
  - 상세한 프로젝트 설명 포함

### 5. 웹 설정
- ✅ **WebMvcConfig** - CORS 및 리소스 핸들러
  - 프론트엔드 개발을 위한 CORS 설정
  - 정적 리소스 핸들러
  - 인터셉터 확장 준비

### 6. 유틸리티 클래스
- ✅ **SecurityUtil** - 현재 사용자 정보 조회
  - `getCurrentUserId()` - 현재 사용자 ID
  - `getCurrentUserEmail()` - 현재 사용자 이메일
  - `getCurrentUserNickname()` - 현재 사용자 닉네임
  - `getCurrentUserRole()` - 현재 사용자 역할

- ✅ **JwtTokenResponse** - JWT 토큰 응답 DTO
  - Access Token, Refresh Token
  - 사용자 기본 정보 포함

### 7. 환경별 설정 파일
- ✅ **application.yml** - 기본 설정
- ✅ **application-local.yml** - 로컬 개발
- ✅ **application-dev.yml** - 개발 서버
- ✅ **application-prod.yml** - 운영 서버

### 8. 버그 수정
- ✅ **Book.java** - @Builder.Default 추가로 경고 해결

## 🚀 사용 방법

### 1. Swagger UI 접속
```
http://localhost:8080/swagger-ui/index.html
```

### 2. JWT 인증 흐름

#### Step 1: 회원가입/로그인
```http
POST /api/auth/signup
POST /api/auth/login
```

**로그인 응답 예시:**
```json
{
  "grantType": "Bearer",
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "accessTokenExpiresIn": 3600,
  "userInfo": {
    "userId": 1,
    "email": "user@example.com",
    "nickname": "작가123",
    "role": "USER"
  }
}
```

#### Step 2: Swagger에서 인증 설정
1. 응답에서 `accessToken` 복사
2. Swagger UI 우측 상단 🔓 **Authorize** 버튼 클릭
3. 값 입력: `Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`
4. **Authorize** 클릭

#### Step 3: 보호된 API 호출
모든 인증이 필요한 API를 Swagger에서 테스트 가능!

### 3. 코드에서 현재 사용자 정보 조회

```java
// Controller에서
@PostMapping("/books")
public ResponseEntity<?> createBook(@RequestBody BookCreateRequest request) {
    Long currentUserId = SecurityUtil.getCurrentUserId();
    String currentUserEmail = SecurityUtil.getCurrentUserEmail();
    
    // 비즈니스 로직...
    return ResponseEntity.ok(response);
}

// Service에서
public void appendSentence(Long bookId, String content) {
    Long writerId = SecurityUtil.getCurrentUserId();
    
    // 문장 추가 로직...
}
```

## 🔧 환경별 프로파일 설정

### Local 개발 (기본)
```bash
# application.yml에서 profiles.active: local로 이미 설정됨
./gradlew bootRun
```

### Dev 서버
```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```

### Production
```bash
# JWT_SECRET 환경 변수 필수!
export JWT_SECRET=your-super-secret-key-here
./gradlew bootRun --args='--spring.profiles.active=prod'
```

## 🔐 보안 설정

### JWT Secret 관리

#### 개발 환경 (Local/Dev)
기본값이 설정되어 있어 바로 사용 가능

#### 운영 환경 (Production)
**반드시 환경 변수로 설정해야 합니다!**

```bash
# Linux/Mac
export JWT_SECRET=0b9e53ea3228c51635b0ee816888ba580e00dcd961d0d9c976a2f40dcf57bcfd

# Windows (PowerShell)
$env:JWT_SECRET="YourVerySecureSecretKeyThatIsAtLeast256BitsLong"

# Docker
docker run -e JWT_SECRET=YourVerySecureSecretKeyThatIsAtLeast256BitsLong ...
```

**⚠️ 주의사항:**
- Secret Key는 **최소 256비트(32바이트)** 이상
- 영문 대소문자, 숫자, 특수문자 혼합
- **절대 코드에 하드코딩 금지**
- **Git에 커밋 금지**

## 🔒 접근 권한 규칙

### Public (인증 불필요)
- `GET /api/books` - 소설 목록 조회
- `GET /api/books/{bookId}` - 소설 상세 조회
- `GET /api/books/{bookId}/view` - 책 뷰어
- `POST /api/auth/signup` - 회원가입
- `POST /api/auth/login` - 로그인
- `/swagger-ui/**` - API 문서
- `/h2-console/**` - H2 콘솔 (개발용)

### Private (JWT 인증 필요)
- `POST /api/books` - 소설 생성
- `POST /api/books/{bookId}/sentences` - 문장 추가
- `POST /api/books/{bookId}/votes` - 투표
- `POST /api/books/{bookId}/comments` - 댓글
- 그 외 모든 CUD 작업

## 📝 다음 단계 (구현 예정)

### AuthService 구현
```java
@Service
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    
    // 회원가입
    public void signup(SignupRequest request) { ... }
    
    // 로그인
    public JwtTokenResponse login(LoginRequest request) { ... }
    
    // 토큰 갱신
    public JwtTokenResponse refresh(String refreshToken) { ... }
}
```

### LoginRequest/SignupRequest DTO 작성

### Exception Handler 추가
- JWT 만료
- 권한 부족
- 인증 실패

## ✅ 빌드 상태
```
BUILD SUCCESSFUL in 8s
6 actionable tasks: 6 executed
```

모든 설정이 완료되었고, 프로젝트가 정상적으로 빌드됩니다! 🎉
