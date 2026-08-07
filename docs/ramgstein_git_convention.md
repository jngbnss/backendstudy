# 램그스타인 Git 컨벤션

## 1. 브랜치 전략

기본 브랜치:

| 브랜치 | 용도 |
| --- | --- |
| main | 배포 가능한 안정 버전 |
| develop | 다음 배포 준비 브랜치 |
| feature/* | 기능 개발 |
| fix/* | 버그 수정 |
| hotfix/* | 운영 긴급 수정 |
| refactor/* | 리팩터링 |
| docs/* | 문서 작업 |
| chore/* | 설정/빌드/잡무 |

브랜치 이름 규칙:

```text
{type}/{issue-number}-{short-description}
```

예시:

```text
feature/12-post-create
fix/18-like-duplicate
docs/3-api-spec
```

## 2. 커밋 메시지

Conventional Commits 기반.

형식:

```text
type(scope): subject
```

subject는 한글로 작성한다.

예시:

```text
feat(post): 게시글 작성 API 추가
fix(report): 중복 신고 방지
docs(api): 신고 API 명세 추가
refactor(auth): 토큰 프로바이더 분리
test(post): 게시글 서비스 테스트 추가
```

### 타입

| 타입 | 의미 |
| --- | --- |
| feat | 기능 추가 |
| fix | 버그 수정 |
| docs | 문서 수정 |
| style | 포맷팅, 세미콜론 등 동작 변화 없는 수정 |
| refactor | 리팩터링 |
| test | 테스트 추가/수정 |
| chore | 빌드, 설정, 패키지 관리 |
| perf | 성능 개선 |
| ci | CI 설정 |
| revert | 이전 커밋 되돌림 |

### Scope 추천

| scope | 영역 |
| --- | --- |
| auth | 인증/인가, JWT, Refresh Token |
| user | 회원 |
| category | 카테고리, 카테고리 신청 |
| post | 게시글, 게시판 |
| upload | 이미지 업로드, S3 |
| like | 좋아요 |
| comment | 댓글 |
| report | 신고 |
| sanction | 회원 제재 |
| admin | 관리자 |
| common | 공통 모듈 |
| infra | 배포/인프라 |

### Subject 규칙

- 한글로 작성
- 무엇을 했는지 명사형 또는 `~ 추가/수정/삭제` 형태로 끝냄
- `type(scope)` 부분은 영어 소문자 유지
- 마침표 없음
- 50자 이내 권장
- 한 커밋은 한 목적만 포함

좋은 예:

```text
feat(category): 카테고리 신청 API 추가
feat(upload): S3 presigned URL 발급 API 추가
fix(like): 중복 좋아요 방지
refactor(post): 생성자에서 타임스탬프 설정
```

나쁜 예:

```text
feat: 게시글도 만들고 로그인도 수정하고 이것저것 함
feat(post): 게시글 작성 API를 추가했습니다.
```

## 3. Pull Request 규칙

PR 제목:

```text
[type] short summary
```

예시:

```text
[feat] 게시글 작성 API 추가
[fix] 중복 좋아요 방지
```

PR 본문 템플릿:

```markdown
## 작업 내용

- 

## 확인 사항

- [ ] 로컬 테스트 통과
- [ ] API 명세와 일치
- [ ] 권한 체크 확인
- [ ] 예외 케이스 확인

## 참고

-
```

## 4. Issue 규칙

Issue 제목:

```text
[domain] task summary
```

예시:

```text
[post] 게시글 작성 API 구현
[report] 신고 처리 관리자 API 구현
```

Issue 본문:

```markdown
## 목적

## 작업 목록

- [ ] 

## 완료 조건

- [ ] 
```

## 5. 코드 리뷰 기준

- 요구사항 만족 여부
- API 명세 일치 여부
- 인증/인가 누락 여부
- 중복 좋아요/중복 신고 방지 여부
- 삭제/숨김/정지 상태 처리 여부
- Refresh Token 저장/폐기 처리 여부
- 카테고리 승인 흐름 처리 여부
- S3 업로드 검증 여부
- 테스트 포함 여부
- 불필요한 리팩터링 포함 여부

## 6. 머지 규칙

- feature 브랜치는 develop으로 PR
- hotfix 브랜치는 main으로 PR 후 develop에도 반영
- 1인 토이 프로젝트는 self-merge 허용
- 협업자가 생기면 최소 1명 승인 후 머지
- CI 실패 시 머지 금지
- squash merge 권장

## 7. 태그 규칙

릴리즈 태그:

```text
v{major}.{minor}.{patch}
```

예시:

```text
v0.1.0
v1.0.0
```

초기 MVP 버전:

```text
v0.1.0
```

