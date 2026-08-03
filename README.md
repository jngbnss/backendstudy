# Board (BASIC)

회원/관리자 없는 **순수 게시판 CRUD** 학습 프로젝트.
**JPA/DB 없이 순수 Spring MVC + 메모리 저장소(Map)** 로 구현. 프론트는 정적 HTML/CSS/JS `fetch`.

## 구조

```text
브라우저 (HTML + JS fetch)
      ↓  /api/posts
Spring Boot REST API (순수 MVC)
      ↓
MemoryPostRepository (ConcurrentHashMap)
```

> 데이터는 메모리에만 있음 → **서버 재시작하면 글이 전부 사라짐(휘발성).**
> 영속 저장이 필요하면 나중에 JDBC/JPA 구현으로 `PostRepository`를 갈아끼우면 됨.

## 실행

```bash
./gradlew bootRun
```

- 화면: http://localhost:8080/  (또는 /index.html)

## API

| 기능 | HTTP | URL | 성공 |
| --- | --- | --- | --- |
| 목록 조회 | GET | `/api/posts` | 200 |
| 상세 조회 | GET | `/api/posts/{postId}` | 200 |
| 작성 | POST | `/api/posts` | 201 |
| 수정 | PUT | `/api/posts/{postId}` | 200 |
| 삭제 | DELETE | `/api/posts/{postId}` | 204 |

작성/수정 요청 본문:

```json
{
  "title": "첫 번째 게시글",
  "content": "게시글 내용입니다.",
  "author": "문종빈"
}
```

## 디렉터리

```text
src/main/java/dev/backendstudy/board
├── BoardApplication.java
├── global/exception/            # 전역 예외 처리(404, 400)
└── domain/post
    ├── Post.java                # 순수 POJO 도메인
    ├── PostController.java      # REST API
    ├── PostService.java
    ├── PostRepository.java      # 저장소 인터페이스
    ├── MemoryPostRepository.java# 메모리(Map) 구현체
    ├── PostCreateRequest.java
    ├── PostUpdateRequest.java
    ├── PostResponse.java
    └── PostNotFoundException.java

src/main/resources/static         # 프론트 (같은 서버에서 서빙 → CORS 불필요)
├── index.html / post-detail.html / post-write.html / post-edit.html
├── css/style.css
└── js/{index,detail,write,edit}.js
```

## 연습 포인트

Controller / Service / Repository(인터페이스+구현 분리) / DTO / CRUD / 예외 처리 / `fetch` / JSON / DOM 조작

## 테스트

```bash
./gradlew test
```

- 게시글 작성 → 201 + 목록 반영
- 제목 빈 값 → 400
- 없는 게시글 조회 → 404
