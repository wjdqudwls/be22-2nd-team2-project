# 📋 Next Page API Specification
> **버전:** 2.1 (Notion Style)
> **최신 업데이트:** 2026-01-15
> **설명:** Next Page 프로젝트의 REST API 상세 명세서입니다.

---

## 🏗️ 1. 회원 (Member) 도메인

### 회원가입

- **API ID**: PGM-AUTH-001
- **1 DEPTH**: 회원
- **2 DEPTH**: 인증
- **3 DEPTH**: 회원가입
- **설명**: 신규 회원을 등록한다.
- **router**: -
- **method**: `POST`
- **URL**: `/api/auth/signup`
- **권한**: `비회원`
- **비고**: -

☑ **Request Parameters (Body)**

| 파라미터명 | 타입 | 필수 | 설명 |
| --- | --- | --- | --- |
| userEmail | STRING | O | 사용자 이메일 (로그인 ID) |
| userPw | STRING | O | 비밀번호 (영문/숫자/특수문자 포함) |
| userNicknm | STRING | O | 사용자 닉네임 |

☑ **Response Parameter**

| 파라미터명 | 타입 | 설명 |
| --- | --- | --- |
| success | BOOLEAN | 성공 여부 |
| data | STRING | 결과 메시지 |

☑ **Success Data Example**

```json
{
  "success": true,
  "code": "SUCCESS",
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": "회원가입 성공"
}
```

---

### 로그인

- **API ID**: PGM-AUTH-002
- **1 DEPTH**: 회원
- **2 DEPTH**: 인증
- **3 DEPTH**: 로그인
- **설명**: 이메일과 비밀번호로 로그인하여 JWT(Access/Refresh Token)를 발급받는다.
- **router**: -
- **method**: `POST`
- **URL**: `/api/auth/login`
- **권한**: `비회원`, `회원`, `관리자`
- **비고**: AccessToken 유효기간 30분, RefreshToken 유효기간 7일

☑ **Request Parameters (Body)**

| 파라미터명 | 타입 | 필수 | 설명 |
| --- | --- | --- | --- |
| userEmail | STRING | O | 사용자 이메일 |
| userPw | STRING | O | 비밀번호 |

☑ **Response Parameter**

| 파라미터명 | 타입 | 설명 |
| --- | --- | --- |
| accessToken | STRING | 액세스 토큰 |
| refreshToken | STRING | 리프레시 토큰 |
| nickname | STRING | 사용자 닉네임 |
| email | STRING | 사용자 이메일 |

☑ **Success Data Example**

```json
{
  "success": true,
  "code": "SUCCESS",
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": {
    "accessToken": "eyJhbGci...",
    "refreshToken": "eyJhbGci...",
    "nickname": "홍길동",
    "email": "test@example.com"
  }
}
```

---

### 내 정보 조회

- **API ID**: PGM-MEMBER-001
- **1 DEPTH**: 회원
- **2 DEPTH**: 마이페이지
- **3 DEPTH**: 내 정보 조회
- **설명**: 로그인한 사용자의 상세 정보를 조회한다.
- **router**: -
- **method**: `GET`
- **URL**: `/api/members/me`
- **권한**: `회원`, `관리자`
- **비고**: -

☑ **Request Header**

| type | value |
| --- | --- |
| Authorization | Bearer {accessToken} |

☑ **Response Parameter**

| 파라미터명 | 타입 | 설명 |
| --- | --- | --- |
| memberId | INTEGER | 사용자 고유 ID |
| email | STRING | 이메일 |
| nickname | STRING | 닉네임 |
| role | STRING | 권한 |
| point | INTEGER | 보유 포인트 |

☑ **Success Data Example**

```json
{
  "success": true,
  "code": "SUCCESS",
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": {
    "memberId": 101,
    "email": "test@example.com",
    "nickname": "홍길동",
    "role": "USER",
    "point": 0
  }
}
```

---

## 📕 2. 소설 (Story) 도메인

### 소설 생성

- **API ID**: PGM-STORY-001
- **1 DEPTH**: 소설
- **2 DEPTH**: 창작
- **3 DEPTH**: 소설 생성
- **설명**: 새로운 소설을 생성하고, 이야기를 시작할 첫 문장을 등록한다.
- **router**: -
- **method**: `POST`
- **URL**: `/api/books`
- **권한**: `회원`, `관리자`
- **비고**: -

☑ **Request Header**

| type | value |
| --- | --- |
| Authorization | Bearer {accessToken} |

☑ **Request Parameters (Body)**

| 파라미터명 | 타입 | 필수 | 설명 |
| --- | --- | --- | --- |
| title | STRING | O | 소설 제목 |
| categoryId | STRING | O | 카테고리 ID |
| maxSequence | INTEGER | O | 최대 문장 수 |
| firstSentence | STRING | O | 첫 문장 내용 |

☑ **Response Parameter**

| 파라미터명 | 타입 | 설명 |
| --- | --- | --- |
| data | INTEGER | 생성된 소설 ID |

☑ **Success Data Example**

```json
{
  "success": true,
  "code": "SUCCESS",
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": 15
}
```

---

### 소설 목록 조회

- **API ID**: PGM-STORY-002
- **1 DEPTH**: 소설
- **2 DEPTH**: 조회
- **3 DEPTH**: 목록 조회
- **설명**: 소설 목록을 페이징, 정렬, 검색 조건에 따라 조회한다.
- **router**: -
- **method**: `GET`
- **URL**: `/api/books`
- **권한**: `비회원`, `회원`, `관리자`
- **비고**: -

☑ **Request Parameters (Query)**

| 파라미터명 | 타입 | 필수 | 설명 |
| --- | --- | --- | --- |
| page | INTEGER | X | 페이지 번호 (0부터 시작) |
| size | INTEGER | X | 페이지 당 개수 (기본 10) |
| sortBy | STRING | X | 정렬 기준 (createdAt, title) |
| keyword | STRING | X | 검색어 |
| categoryId | STRING | X | 카테고리 필터 |

☑ **Response Parameter**

| 파라미터명 | 타입 | 설명 |
| --- | --- | --- |
| content | ARRAY | 소설 목록 데이터 |
| totalElements | INTEGER | 전체 소설 개수 |
| totalPages | INTEGER | 전체 페이지 수 |

☑ **Success Data Example**

```json
{
  "success": true,
  "code": "SUCCESS",
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": {
    "content": [
      {
        "id": 15,
        "title": "재미있는 소설",
        "writerNickname": "홍길동",
        "createdAt": "2026-01-15T12:00:00"
      }
    ],
    "totalElements": 1,
    "totalPages": 1
  }
}
```

---

### 소설 상세 조회

- **API ID**: PGM-STORY-003
- **1 DEPTH**: 소설
- **2 DEPTH**: 조회
- **3 DEPTH**: 상세 조회
- **설명**: 특정 소설의 상세 정보와 현재까지 작성된 문장 목록을 조회한다.
- **router**: -
- **method**: `GET`
- **URL**: `/api/books/{bookId}`
- **권한**: `비회원`, `회원`, `관리자`
- **비고**: -

☑ **Response Parameter**

| 파라미터명 | 타입 | 설명 |
| --- | --- | --- |
| id | INTEGER | 소설 ID |
| title | STRING | 제목 |
| content | STRING | 전체 내용 (문장 합본) |
| writer | STRING | 작성자 닉네임 |
| category | STRING | 카테고리 |
| sentences | ARRAY | 문장 목록 리스트 |

☑ **Success Data Example**

```json
{
  "success": true,
  "code": "SUCCESS",
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": {
    "id": 15,
    "title": "재미있는 소설",
    "content": "첫 문장. 두 번째 문장.",
    "writer": "홍길동",
    "category": "FANTASY",
    "sentences": [
      { "id": 1, "content": "첫 문장.", "writer": "홍길동" },
      { "id": 2, "content": "두 번째 문장.", "writer": "김철수" }
    ]
  }
}
```

---

## 💬 3. 반응 (Reaction) 도메인

### 댓글 작성

- **API ID**: PGM-REACTION-001
- **1 DEPTH**: 반응
- **2 DEPTH**: 댓글
- **3 DEPTH**: 댓글 작성
- **설명**: 소설에 새로운 댓글(또는 대댓글)을 작성한다.
- **router**: -
- **method**: `POST`
- **URL**: `/api/reactions/comments`
- **권한**: `회원`, `관리자`
- **비고**: -

☑ **Request Header**

| type | value |
| --- | --- |
| Authorization | Bearer {accessToken} |

☑ **Request Parameters (Body)**

| 파라미터명 | 타입 | 필수 | 설명 |
| --- | --- | --- | --- |
| bookId | INTEGER | O | 소설 ID |
| content | STRING | O | 댓글 내용 |
| parentId | INTEGER | X | 부모 댓글 ID (대댓글 시) |

☑ **Response Parameter**

| 파라미터명 | 타입 | 설명 |
| --- | --- | --- |
| data | INTEGER | 생성된 댓글 ID |

☑ **Success Data Example**

```json
{
  "success": true,
  "code": "SUCCESS",
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": 42
}
```

---

### 투표 (좋아요/싫어요)

- **API ID**: PGM-REACTION-002
- **1 DEPTH**: 반응
- **2 DEPTH**: 투표
- **3 DEPTH**: 좋아요/싫어요
- **설명**: 소설 또는 문장에 대해 좋아요/싫어요 투표를 한다.
- **router**: -
- **method**: `POST`
- **URL**: `/api/reactions/votes/books` (소설)
- **권한**: `회원`, `관리자`
- **비고**: 문장 투표는 `/api/reactions/votes/sentences/{id}` 사용

☑ **Request Header**

| type | value |
| --- | --- |
| Authorization | Bearer {accessToken} |

☑ **Request Parameters (Body)**

| 파라미터명 | 타입 | 필수 | 설명 |
| --- | --- | --- | --- |
| bookId | INTEGER | O | 소설 ID (소설 투표 시) |
| voteType | STRING | O | 투표 유형 (LIKE, DISLIKE) |

☑ **Success Data Example**

```json
{
  "success": true,
  "code": "SUCCESS",
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": true
}
```
