# 🚀 Backend Study: Board Project

본 프로젝트는 기초적인 게시판 구현을 시작으로, 백엔드 설계 원칙과 성능 최적화를 단계별로 적용하며 학습하는 개인 스터디 프로젝트입니다.

## 📌 Project Roadmap
- [x] **Step 1: Basic CRUD** - 기본적인 게시글 작성/조회/수정/삭제 구현
- [ ] **Step 2: Search & Pagination** - 페이징 처리 및 검색 기능 추가
- [ ] **Step 3: Optimization** - Querydsl 도입 및 N+1 문제 해결

### ✅ Step 1 상세 요구사항 (Issue 관리)
모든 요구사항은 [GitHub Issues](https://github.com/jngbnss/backendstudy/issues)를 통해 관리됩니다.
- [x] 게시글 작성하기 (#1)
- [x] 게시글 리스트 조회 (#2)
- [x] 게시글 상세 조회 (#3)
- [ ] 게시글 수정 (#4)
- [ ] 게시글 삭제 (#5)

---

## 🛠 Tech Stack (Step 1)
* **Language:** Java 21
* **Framework:** Spring Boot 3.x
* **ORM:** Spring Data JPA
* **Database:** H2 (In-memory)
* **Build Tool:** Gradle

---

## 📊 Database Schema (ERD)

### Board Entity
| Column | Type | Description |
| :--- | :--- | :--- |
| `id` | Long (PK) | 게시글 고유 식별자 |
| `title` | String | 게시글 제목 |
| `writer` | String | 작성자 이름 |
| `content` | Text | 게시글 본문 |
| `created_at` | LocalDateTime | 최초 작성 시간 |
| `updated_at` | LocalDateTime | 최종 수정 시간 |

---

## 💡 개발 원칙 및 피드백 반영 (Log)

학습 과정에서 수렴한 피드백을 바탕으로 적용한 핵심 설계 원칙입니다.

### 1. 작업 관리 및 워크플로우
* **이슈 기반 개발:** 단순 텍스트 기록 대신 GitHub Issue를 생성하여 요구사항을 정의하고, 작업 단위를 명확히 분리합니다.
* **커밋 컨벤션 준수:** `feat`, `fix`, `docs`, `chore` 등의 접두어를 사용하여 변경 이력의 가독성을 높입니다.

### 2. 아키텍처 및 설계 개선
* **DTO(Data Transfer Object) 분리:** * 엔티티 객체가 API 명세에 직접 노출되어 발생하는 의존성 문제를 방지합니다.
    * 요청(`RequestDto`)과 응답(`ResponseDto`) 전용 객체를 사용하여 계층 간 데이터를 안전하게 전달합니다.
* **트랜잭션(Transaction) 관리 최적화:** * 서비스 계층 상단에 `@Transactional(readOnly = true)`를 공통 적용하여 성능(스냅샷 저장 방지 등)을 최적화하고 데이터 오수정을 방지합니다.
    * 데이터 변경이 발생하는 메서드에만 `@Transactional`을 개별 부여하여 쓰기 권한을 제한적으로 관리합니다.

