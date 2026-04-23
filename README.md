
---

# 🚀 Backend Study: Board Project

본 프로젝트는 기초적인 게시판 구현을 시작으로, 백엔드 설계 원칙과 성능 최적화를 단계별로 적용하며 학습하는 개인 스터디 프로젝트입니다.

## 📌 Project Roadmap

- [x] **Step 1: Basic CRUD** - 기본적인 게시글 작성/조회/수정/삭제 구현


---

## 🛠 Tech Stack (Step 1)
* **Language:** Java 21
* **Framework:** Spring Boot 3.x
* **ORM:** Spring Data JPA
* **Database:** H2 (Development)
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


### 1. 작업 관리 및 워크플로우
* **이슈 기반 개발:** 모든 요구사항과 피드백은 GitHub Issue를 생성하여 기록합니다.
* **커밋 컨벤션 준수:**
    * `feat`: 새로운 기능 추가
    * `fix`: 버그 수정
    * `docs`: 문서 수정
    * `chore`: 설정 및 기타 변경

### 2. 아키텍처 및 설계 개선
* **DTO(Data Transfer Object) 활용:** 엔티티 객체의 변경이 API 명세에 직접적인 영향을 주지 않도록, 요청(Request)과 응답(Response)에 전용 DTO를 사용합니다.
* **트랜잭션 관리 최적화:** * 서비스 계층 상단에 `@Transactional(readOnly = true)`를 선언하여 조회 성능을 높이고 의도치 않은 데이터 수정을 방지합니다.
    * 쓰기 작업이 필요한 메서드에만 `@Transactional`을 개별 부여하여 범위를 명확히 합니다.

---



// 피드백
요구사항 이슈에서 관리하기
dto 파일 변경하기
트랜잭션 리드 온리 한곳으로 모으기
