# 램그스타인 API 명세서

## 1. 공통 규칙

Base URL:

```text
/api/v1
```

인증:

```http
Authorization: Bearer {accessToken}
```

모든 API 응답은 `success/data/error` 래퍼를 사용한다. 이 문서의 Response 예시는 실제 응답 전체를 기준으로 작성한다.

성공 응답:

```json
{
  "success": true,
  "data": {},
  "error": null
}
```

실패 응답:

```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "POST_NOT_FOUND",
    "message": "게시글을 찾을 수 없습니다."
  }
}
```

페이지네이션은 1-based를 사용한다.

```json
{
  "success": true,
  "data": {
    "items": [],
    "page": 1,
    "size": 20,
    "totalCount": 120,
    "totalPages": 6
  },
  "error": null
}
```

## 2. Auth API

### 회원가입

```http
POST /api/v1/auth/signup
```

Request:

```json
{
  "email": "user@example.com",
  "password": "password123!",
  "nickname": "fail_master"
}
```

Response:

```json
{
  "success": true,
  "data": {
    "id": 1,
    "email": "user@example.com",
    "nickname": "fail_master"
  },
  "error": null
}
```

### 로그인

```http
POST /api/v1/auth/login
```

Request:

```json
{
  "email": "user@example.com",
  "password": "password123!"
}
```

Response:

```json
{
  "success": true,
  "data": {
    "accessToken": "jwt-access-token",
    "refreshToken": "jwt-refresh-token",
    "user": {
      "id": 1,
      "nickname": "fail_master",
      "role": "USER",
      "status": "ACTIVE"
    }
  },
  "error": null
}
```

### 토큰 재발급

```http
POST /api/v1/auth/refresh
```

Request:

```json
{
  "refreshToken": "jwt-refresh-token"
}
```

Response:

```json
{
  "success": true,
  "data": {
    "accessToken": "new-jwt-access-token",
    "refreshToken": "new-jwt-refresh-token"
  },
  "error": null
}
```

### 로그아웃

```http
POST /api/v1/auth/logout
```

Auth required.

Request:

```json
{
  "refreshToken": "jwt-refresh-token"
}
```

Response:

```json
{
  "success": true,
  "data": {
    "loggedOut": true
  },
  "error": null
}
```

## 3. User API

### 내 정보 조회

```http
GET /api/v1/users/me
```

Auth required.

Response:

```json
{
  "success": true,
  "data": {
    "id": 1,
    "email": "user@example.com",
    "nickname": "fail_master",
    "profileImageUrl": null,
    "bio": "실패 수집 중",
    "role": "USER",
    "status": "ACTIVE",
    "suspendedUntil": null
  },
  "error": null
}
```

### 내 정보 수정

```http
PATCH /api/v1/users/me
```

Auth required.

Request:

```json
{
  "nickname": "new_fail_master",
  "profileImageUrl": "https://cdn.example.com/profile.png",
  "bio": "망한 이야기를 모읍니다."
}
```

### 회원 탈퇴

```http
DELETE /api/v1/users/me
```

Auth required.

Response:

```json
{
  "success": true,
  "data": {
    "deleted": true
  },
  "error": null
}
```

## 4. Category API

### 카테고리 목록 조회

```http
GET /api/v1/categories
```

Response:

```json
{
  "success": true,
  "data": {
    "items": [
      {
        "id": 1,
        "name": "회사",
        "slug": "work",
        "description": "회사와 업무 실패담",
        "status": "ACTIVE",
        "sortOrder": 1
      }
    ]
  },
  "error": null
}
```

### 카테고리 신청

```http
POST /api/v1/category-requests
```

Auth required.

Request:

```json
{
  "name": "운동",
  "description": "운동하다 망한 실패담 게시판이 있으면 좋겠습니다."
}
```

Response:

```json
{
  "success": true,
  "data": {
    "id": 10,
    "status": "PENDING"
  },
  "error": null
}
```

### 내 카테고리 신청 목록

```http
GET /api/v1/category-requests/me?page=1&size=20
```

Auth required.

## 5. Board/Post API

### 전체 게시판

```http
GET /api/v1/posts?page=1&size=20&sort=latest
```

카테고리 필터 없이 공개 게시글 전체를 조회한다.

### 카테고리별 게시판

```http
GET /api/v1/categories/{categorySlug}/posts?page=1&size=20&sort=latest
```

### 인기 게시판

```http
GET /api/v1/posts/popular?page=1&size=20&period=weekly
```

Query:

| 이름 | 필수 | 설명 |
| --- | --- | --- |
| page | N | 1-based 페이지 번호 |
| size | N | 페이지 크기 |
| sort | N | latest, popular |
| period | N | daily, weekly, monthly, all |

목록 Response:

```json
{
  "success": true,
  "data": {
    "items": [
      {
        "id": 10,
        "author": {
          "id": 1,
          "nickname": "fail_master",
          "profileImageUrl": null
        },
        "category": {
          "id": 1,
          "name": "회사",
          "slug": "work"
        },
        "title": "배포 버튼 잘못 눌렀다",
        "contentPreview": "금요일 오후에...",
        "thumbnailUrl": "https://cdn.example.com/posts/10/1.png",
        "likeCount": 12,
        "commentCount": 3,
        "reportCount": 0,
        "likedByMe": false,
        "createdAt": "2026-08-03T10:00:00"
      }
    ],
    "page": 1,
    "size": 20,
    "totalCount": 1,
    "totalPages": 1
  },
  "error": null
}
```

### 게시글 상세 조회

```http
GET /api/v1/posts/{postId}
```

Response:

```json
{
  "success": true,
  "data": {
    "id": 10,
    "author": {
      "id": 1,
      "nickname": "fail_master",
      "profileImageUrl": null
    },
    "category": {
      "id": 1,
      "name": "회사",
      "slug": "work"
    },
    "title": "배포 버튼 잘못 눌렀다",
    "content": "금요일 오후에 운영 배포를...",
    "visibility": "PUBLIC",
    "status": "PUBLISHED",
    "images": [
      {
        "id": 1,
        "imageUrl": "https://cdn.example.com/posts/10/1.png",
        "sortOrder": 1
      }
    ],
    "likeCount": 12,
    "commentCount": 3,
    "reportCount": 0,
    "likedByMe": false,
    "reportedByMe": false,
    "createdAt": "2026-08-03T10:00:00",
    "updatedAt": "2026-08-03T10:00:00"
  },
  "error": null
}
```

### 게시글 작성

```http
POST /api/v1/posts
```

Auth required.

Request:

```json
{
  "title": "배포 버튼 잘못 눌렀다",
  "content": "금요일 오후에 운영 배포를...",
  "categoryId": 1,
  "visibility": "PUBLIC",
  "images": [
    {
      "imageUrl": "https://cdn.example.com/posts/10/1.png",
      "objectKey": "posts/10/1.png",
      "sortOrder": 1
    }
  ]
}
```

Response:

```json
{
  "success": true,
  "data": {
    "id": 10
  },
  "error": null
}
```

### 게시글 수정

```http
PATCH /api/v1/posts/{postId}
```

Auth required. 작성자만 가능.

Request:

```json
{
  "title": "배포 버튼 진짜 잘못 눌렀다",
  "content": "수정된 본문",
  "categoryId": 1,
  "visibility": "PUBLIC",
  "images": []
}
```

### 게시글 삭제

```http
DELETE /api/v1/posts/{postId}
```

Auth required. 작성자 또는 관리자만 가능.

## 6. Upload API

### S3 Presigned URL 발급

```http
POST /api/v1/uploads/presigned-url
```

Auth required.

Request:

```json
{
  "fileName": "fail.png",
  "contentType": "image/png",
  "fileSize": 1048576
}
```

Response:

```json
{
  "success": true,
  "data": {
    "uploadUrl": "https://s3.amazonaws.com/bucket/posts/temp/fail.png?...",
    "imageUrl": "https://cdn.example.com/posts/temp/fail.png",
    "objectKey": "posts/temp/fail.png",
    "expiresIn": 300
  },
  "error": null
}
```

## 7. Like API

### 좋아요

```http
POST /api/v1/posts/{postId}/likes
```

Auth required.

Response:

```json
{
  "success": true,
  "data": {
    "postId": 10,
    "liked": true,
    "likeCount": 13
  },
  "error": null
}
```

### 좋아요 취소

```http
DELETE /api/v1/posts/{postId}/likes
```

Auth required.

Response:

```json
{
  "success": true,
  "data": {
    "postId": 10,
    "liked": false,
    "likeCount": 12
  },
  "error": null
}
```

## 8. Comment API

### 댓글 목록 조회

```http
GET /api/v1/posts/{postId}/comments?page=1&size=20
```

Response:

```json
{
  "success": true,
  "data": {
    "items": [
      {
        "id": 100,
        "author": {
          "id": 2,
          "nickname": "another_fail"
        },
        "content": "이건 진짜 아프다...",
        "createdAt": "2026-08-03T10:10:00",
        "updatedAt": "2026-08-03T10:10:00"
      }
    ],
    "page": 1,
    "size": 20,
    "totalCount": 1,
    "totalPages": 1
  },
  "error": null
}
```

### 댓글 작성

```http
POST /api/v1/posts/{postId}/comments
```

Auth required.

Request:

```json
{
  "content": "이건 진짜 아프다..."
}
```

### 댓글 수정

```http
PATCH /api/v1/comments/{commentId}
```

Auth required. 작성자만 가능.

Request:

```json
{
  "content": "수정된 댓글"
}
```

### 댓글 삭제

```http
DELETE /api/v1/comments/{commentId}
```

Auth required. 작성자 또는 관리자만 가능.

## 9. Report API

### 게시글 신고

```http
POST /api/v1/posts/{postId}/reports
```

Auth required.

Request:

```json
{
  "reason": "FAKE_FAILURE",
  "detail": "실패담이 아니라 특정 사람을 조롱하는 내용입니다."
}
```

신고 사유:

| 값 | 설명 |
| --- | --- |
| FAKE_FAILURE | 진짜 실패담이 아님 |
| MOCKING | 조롱/기만 목적 |
| HARASSMENT | 괴롭힘/혐오 |
| SPAM | 스팸 |
| ETC | 기타 |

Response:

```json
{
  "success": true,
  "data": {
    "id": 500,
    "postId": 10,
    "status": "PENDING"
  },
  "error": null
}
```

### 내 신고 목록

```http
GET /api/v1/reports/me?page=1&size=20
```

Auth required.

## 10. Admin API

### 카테고리 신청 목록 조회

```http
GET /api/v1/admin/category-requests?status=PENDING&page=1&size=20
```

Admin required.

### 카테고리 신청 처리

```http
PATCH /api/v1/admin/category-requests/{requestId}
```

Admin required.

승인 Request:

```json
{
  "status": "APPROVED",
  "slug": "exercise",
  "sortOrder": 5
}
```

반려 Request:

```json
{
  "status": "REJECTED",
  "rejectReason": "기존 카테고리와 범위가 겹칩니다."
}
```

### 신고 목록 조회

```http
GET /api/v1/admin/reports?status=PENDING&page=1&size=20
```

Admin required.

### 신고 처리

```http
PATCH /api/v1/admin/reports/{reportId}
```

Admin required.

Request:

```json
{
  "status": "ACCEPTED",
  "action": "TEMP_SUSPEND_USER",
  "memo": "조롱 목적 게시글로 판단",
  "sanction": {
    "days": 7,
    "reason": "조롱 목적 게시글 작성"
  }
}
```

Action:

| 값 | 설명 |
| --- | --- |
| NONE | 조치 없음 |
| HIDE_POST | 게시글 숨김 |
| DELETE_POST | 게시글 삭제 |
| TEMP_SUSPEND_USER | 작성자 일시정지 |
| PERMANENT_SUSPEND_USER | 작성자 영구정지 |

### 게시글 강제 숨김

```http
PATCH /api/v1/admin/posts/{postId}/hide
```

Admin required.

### 회원 일시정지

```http
PATCH /api/v1/admin/users/{userId}/temporary-suspension
```

Admin required.

Request:

```json
{
  "reason": "반복적인 허위 실패담 업로드",
  "days": 7
}
```

### 회원 영구정지

```http
PATCH /api/v1/admin/users/{userId}/permanent-suspension
```

Admin required.

Request:

```json
{
  "reason": "반복적인 조롱성 게시글 작성"
}
```

### 회원 정지 해제

```http
PATCH /api/v1/admin/users/{userId}/unsuspend
```

Admin required.

Request:

```json
{
  "reason": "관리자 재검토 후 해제"
}
```

## 11. 에러 코드

| 코드 | HTTP | 설명 |
| --- | --- | --- |
| AUTH_REQUIRED | 401 | 인증 필요 |
| INVALID_TOKEN | 401 | 토큰 오류 |
| EXPIRED_TOKEN | 401 | 토큰 만료 |
| FORBIDDEN | 403 | 권한 없음 |
| USER_NOT_FOUND | 404 | 회원 없음 |
| POST_NOT_FOUND | 404 | 게시글 없음 |
| CATEGORY_NOT_FOUND | 404 | 카테고리 없음 |
| COMMENT_NOT_FOUND | 404 | 댓글 없음 |
| REPORT_NOT_FOUND | 404 | 신고 없음 |
| DUPLICATE_EMAIL | 409 | 이메일 중복 |
| DUPLICATE_NICKNAME | 409 | 닉네임 중복 |
| DUPLICATE_CATEGORY | 409 | 카테고리 중복 |
| ALREADY_LIKED | 409 | 이미 좋아요 누름 |
| ALREADY_REPORTED | 409 | 이미 신고함 |
| INVALID_REPORT_REASON | 400 | 신고 사유 오류 |
| INVALID_FILE_TYPE | 400 | 이미지 타입 오류 |
| FILE_TOO_LARGE | 400 | 이미지 크기 초과 |
| VALIDATION_ERROR | 400 | 입력값 오류 |

