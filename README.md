# Board (BASIC)

회원/관리자 없는 **순수 게시판 CRUD** 학습 프로젝트. Spring Boot REST API + 정적 프론트(HTML/CSS/JS `fetch`).

## 구조

```text
브라우저 (HTML + JS fetch)
      ↓  /api/posts
Spring Boot REST API
      ↓
JPA (Spring Data)
      ↓
H2 (파일 모드)
```

## 실행

```bash
./gradlew bootRun
```

- 화면: http://localhost:8080/index.html
- DB 콘솔: http://localhost:8080/h2-console  (JDBC URL: `jdbc:h2:file:./data/boarddb`)

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
├── config/JpaAuditingConfig.java
├── global/exception/        # 전역 예외 처리(404, 400)
└── domain/post
    ├── Post.java            # 엔티티 (더티 체킹 update)
    ├── PostController.java  # REST API
    ├── PostService.java
    ├── PostRepository.java
    ├── PostCreateRequest.java
    ├── PostUpdateRequest.java
    ├── PostResponse.java
    └── PostNotFoundException.java

src/main/resources/static     # 프론트 (같은 서버에서 서빙 → CORS 불필요)
├── index.html               # 목록
├── post-detail.html         # 상세
├── post-write.html          # 작성
├── post-edit.html           # 수정
├── css/style.css
└── js/{index,detail,write,edit}.js
```

## 연습 포인트

Entity / Repository / Service / Controller / DTO / CRUD / JPA 더티 체킹 / 예외 처리 / `fetch` / JSON / DOM 조작

## 테스트

```bash
./gradlew test
```

- 게시글 작성 → 201 + 목록 반영
- 제목 빈 값 → 400
- 없는 게시글 조회 → 404
