# 🏗️ Next Page : Microservices Architecture (MSA) Project

> **우리가 함께 만드는 실시간 릴레이 소설 - MSA 버전**
> 본 디렉토리는 `next-page` 모놀리식 프로젝트를 3개의 도메인 서비스와 인프라 서비스로 분리한 전문 MSA 프로젝트입니다.

---

## 📑 프로젝트 개요

- **목적:** 도메인 분리를 통한 확장성 확보 및 장애 격리
- **아키텍처:** Spring Cloud 기반 Microservices Architecture
- **핵심 기술:** 
  - **Service Discovery:** Netflix Eureka
  - **API Gateway:** Spring Cloud Gateway (JWT Auth Filter)
  - **Configuration:** Spring Cloud Config (Git Repository 연동)
  - **Communication:** OpenFeign + Resilience4j (Circuit Breaker)
  - **Database:** MariaDB (Database per Service)

---

## 🏗️ 시스템 구성도

```
                    [Config Server] (8888)
                           │
                           ▼
                    [Discovery Server] (8761)
                           │
                           ▼
                    [API Gateway Server] (8000)
                           │
            ┌─────────────┴─────────────┐
            │             │             │
    [Member Service] [Story Service] [Reaction Service]
       (8081)          (8082 - WS)      (8083)
            │             │             │
    [DB: member]     [DB: story]     [DB: reaction]
```

---

## 🚦 시작하기 (Quick Start)

### 1. 데이터베이스 설정
`database-scripts` 디렉토리의 가이드를 따라 3개의 독립적인 데이터베이스를 생성합니다.
- [Database Setup Guide](database-scripts/README.md)

### 2. 서비스 실행 순서
서비스 간 의존성으로 인해 아래 순서대로 기동하는 것을 권장합니다.

1.  **config-server:** 설정 중앙 관리
2.  **discovery-server:** 유레카 서비스 등록
3.  **member-service:** 회원 및 인증 (다른 서비스의 Feign 타겟)
4.  **story-service:** 소설 및 실시간 WebSocket
5.  **reaction-service:** 댓글 및 투표
6.  **gateway-server:** 최종 진입점

```bash
# 루트 디렉토리에서 전체 빌드
./gradlew clean build -x test

# 또는 개별 기동
./gradlew :member-service:bootRun
```

### 3. 프론트엔드 (Vue 3) 실행
별도의 Node.js 설치 없이 Gradle을 통해 바로 실행할 수 있습니다.

```bash
# 의존성 설치 및 개발 서버 시작
.\gradlew :frontend:start
# 접속: http://localhost:3000
```

> **참고:** Node.js가 설치된 환경이라면 `frontend` 디렉토리에서 직접 `npm install && npm run dev`를 실행해도 됩니다.

---

## 📚 관련 문서

- **[MSA 전환 완료 보고서](MSA_IMPLEMENTATION_COMPLETE.md):** 전환 과정 및 변경 사항 상세
- **[MSA 구축 현황판](MSA_SETUP_STATUS.md):** 콤포넌트별 구현 상태
- **[통합 개발자 가이드](../DEVELOPER_GUIDE.md):** 아키텍처 및 코딩 컨벤션

---

**Last Updated:** 2026-01-15
**Status:** ✅ Production Ready
