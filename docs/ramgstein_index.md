# 램그스타인 산출물

개인 토이 프로젝트 램그스타인 초기 기획 문서.

현재 정본 결정:

- API 응답은 `success/data/error` 래퍼로 통일
- 카테고리는 고정 enum이 아니라 관리자 승인형 게시판 구조
- 전체 게시판과 인기 게시판 포함
- 인증은 JWT + Refresh Token
- 신고 상태와 관리자 조치 분리
- 회원 제재는 일시정지/영구정지/해제 가능
- 탈퇴 기능 포함
- 이미지는 S3 Presigned URL 방식

| 문서 | 설명 |
| --- | --- |
| [요구사항 명세서](./ramgstein_requirements.md) | MVP 범위, 기능 요구사항, 비기능 요구사항 |
| [ERD](./ramgstein_erd.md) | 회원, 토큰, 카테고리, 게시글, 이미지, 좋아요, 댓글, 신고, 제재 |
| [API 명세서](./ramgstein_api_spec.md) | REST API 엔드포인트, 요청/응답 예시, 에러 코드 |
| [Git 컨벤션](./ramgstein_git_convention.md) | 브랜치, 커밋, PR, 이슈 규칙 |

