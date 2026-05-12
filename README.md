# Backend Study: Blog Project

Spring Boot 기반 블로그 게시판 학습 프로젝트입니다. 게시글 CRUD를 시작으로 회원가입, 로그인, 회원 조회, 회원 정보 수정 기능을 단계적으로 구현합니다.

## Project Roadmap

- [x] Step 1: 게시글 기본 CRUD
- [x] Step 2: 회원가입
- [x] Step 3: 로그인/로그아웃
- [x] Step 4: 회원 조회
- [x] Step 5: 회원 정보 수정
- [ ] Step 6: 로그인한 회원만 게시글 작성
- [ ] Step 7: 회원 탈퇴
- [ ] Step 8: 예외 처리 정리
- [ ] Step 9: 검색 및 페이지
- [ ] Step 10: 성능 최적화

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
- 내 프로필 조회
- 내 이름 수정
- 로그인 상태에서만 게시글 목록의 My Profile 버튼 표시

## View And API Structure

현재는 Spring MVC와 Thymeleaf 학습을 위해 View Controller와 REST API Controller를 함께 사용합니다.

### View

- `BlogViewController`: 게시글 화면 반환
- `MemberViewController`: 회원가입, 로그인, 내 프로필 화면 반환

### API

- `BoardController`: 게시글 REST API 처리
- `MemberController`: 회원 REST API 처리
- `ReplyController`: 댓글 REST API 처리

### Template Directory

```text
templates/
  boards/
    boardList.html
    boardDetail.html
    boardWrite.html
    boardUpdate.html
  members/
    login.html
    signup.html
    myProfile.html
```

## Member Profile Flow

1. 로그인 성공 시 세션에 `loginMemberId`를 저장합니다.
2. `/boards` 화면에서는 세션에 `loginMemberId`가 있을 때만 `My Profile` 버튼을 보여줍니다.
3. `/members/me` 화면에서는 세션의 회원 id로 회원 정보를 조회해 이메일과 이름을 표시합니다.
4. 이메일은 조회만 가능하고, 이름만 수정할 수 있습니다.
5. 이름 수정 요청은 `PUT /api/members/me`로 처리합니다.

## Next Task

게시글 작성은 로그인한 회원만 가능하도록 변경할 예정입니다. 이후 작성자 이름도 사용자가 직접 입력하지 않고 세션의 로그인 회원 정보로 저장하는 흐름으로 정리합니다.

## Git Convention

앞으로 커밋 메시지는 아래 컨벤션을 따릅니다.

```text
type: subject
```

### Commit Type

| Type | Description |
| :--- | :--- |
| `feat` | 새로운 기능 추가 |
| `fix` | 버그 수정 |
| `docs` | 문서 수정 |
| `style` | 코드 포맷, 세미콜론, 공백 등 기능 변경 없는 수정 |
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
- GitHub에 올리는 커밋도 이 컨벤션을 따릅니다.
