# Backend Study: Blog Project

Spring Boot 기반 블로그/게시판 학습 프로젝트입니다. 게시글 CRUD를 시작으로 회원가입, 로그인, 회원 조회, 회원정보 수정, 회원탈퇴 기능을 단계적으로 구현합니다.

## Project Roadmap

- [x] Step 1: 게시글 기본 CRUD
- [x] Step 2: 회원가입
- [x] Step 3: 로그인/로그아웃
- [ ] Step 4: 회원 조회
- [ ] Step 5: 회원정보 수정
- [ ] Step 6: 회원탈퇴
- [ ] Step 7: 예외 처리 정리
- [ ] Step 8: 검색 및 페이징
- [ ] Step 9: 성능 최적화

## Tech Stack

- Java 21
- Spring Boot 3.x
- Spring Data JPA
- Thymeleaf
- H2 Database
- Gradle

## Current Features

### Board

- 게시글 목록 조회
- 게시글 상세 조회
- 게시글 작성 화면
- 게시글 수정 화면

### Member

- 회원가입
- 로그인
- 로그아웃
- 세션 기반 로그인 상태 저장

## Git Convention

앞으로 커밋을 올릴 때는 아래 컨벤션을 지킵니다.

```text
type: subject
```

### Commit Type

| Type | Description |
| :--- | :--- |
| `feat` | 새로운 기능 추가 |
| `fix` | 버그 수정 |
| `docs` | 문서 수정 |
| `style` | 코드 포맷팅, 세미콜론, 공백 등 기능 변경 없는 수정 |
| `refactor` | 기능 변경 없는 코드 구조 개선 |
| `test` | 테스트 코드 추가 또는 수정 |
| `chore` | 빌드, 설정, 패키지 관리 등 기타 작업 |

### Commit Message Examples

```text
feat: 회원 로그인 기능 추가
fix: 게시글 상세 조회 오류 수정
docs: README에 Git 컨벤션 추가
refactor: 게시글 화면 컨트롤러 경로 정리
```

### Rules

- 커밋 메시지는 `type: subject` 형식을 사용합니다.
- subject는 변경 내용을 짧고 명확하게 작성합니다.
- 하나의 커밋에는 가능한 한 하나의 목적만 담습니다.
- 앞으로 GitHub에 올리는 커밋은 이 컨벤션을 따릅니다.

## View And API Structure

현재는 Spring MVC와 Thymeleaf 학습을 위해 View Controller와 HTML 템플릿을 함께 사용합니다.

- `BlogViewController`: 게시글 화면 반환
- `MemberViewController`: 회원가입/로그인 화면 반환
- `MemberController`: 회원 관련 REST API 처리

나중에 REST API + JSON 방식으로 전환하면 View Controller는 제거하거나 최소화하고, `@RestController` 중심으로 정리할 예정입니다.
