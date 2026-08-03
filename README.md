# Backend Study (fresh start)

Spring Boot 기반 백엔드 학습 프로젝트. `develop` 브랜치에서 새로 시작합니다.

## Branch Strategy

| Branch | 용도 |
| :--- | :--- |
| `main` | 배포 전용. 항상 동작하는 상태만 올라감 (AWS 배포 대상) |
| `develop` | 통합 브랜치. 기능 작업을 여기 모음 |
| `feature/*` | 기능별 작업 브랜치. `develop`에서 분기 |
| `old` | 이전 버전 코드 백업 (참고용, 손대지 않음) |

작업 흐름: `feature/*` → `develop` 머지 → 검증 → 준비되면 `main` 교체/머지 → 배포

## Tech Stack

- Java 21
- Spring Boot 3.x
- Gradle

## Git Convention

```text
type: subject
```

| Type | Description |
| :--- | :--- |
| `feat` | 새로운 기능 추가 |
| `fix` | 버그 수정 |
| `docs` | 문서 수정 |
| `style` | 포맷 등 기능 변경 없는 수정 |
| `refactor` | 기능 변경 없는 코드 구조 개선 |
| `test` | 테스트 코드 추가/수정 |
| `chore` | 빌드/설정 등 기타 |

- 커밋은 `type: subject` 형식.
- 하나의 커밋에 하나의 목적.

## Roadmap

- [ ] 요구사항 정리 (docs/requirements)
- [ ] API 명세서 (Swagger 또는 docs/api)
- [ ] 도메인 설계 / ERD
- [ ] 기능 구현
