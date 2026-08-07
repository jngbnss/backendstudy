package dev.backendstudy.ramgstein.post;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {
    /*
     * ── JpaRepository 가 공짜로 주는 메서드 ──
     *
     * [저장]
     * save(post)                    저장 + 수정 (id 없으면 INSERT, 있으면 UPDATE)
     * saveAll(list)                 여러 개 저장
     * saveAndFlush(post)            저장 후 즉시 DB 반영 (보통 불필요)
     * saveAllAndFlush(list)         위의 여러 개 버전
     *
     * [조회]
     * findById(id)                  Optional<Post> 반환. 없으면 empty
     * findAll()                     List<Post> 전체
     * findAll(sort)                 정렬해서 전체
     * findAll(pageable)             Page<Post> — 페이지네이션용
     * findAllById(ids)              id 목록으로 여러 개
     * getReferenceById(id)          프록시만 반환. 실제 SELECT 안 날림 ⚠️
     * existsById(id)                있냐 없냐 boolean
     * count()                       전체 개수
     *
     * [삭제]
     * delete(post)                  엔티티로 삭제
     * deleteById(id)                id로 삭제
     * deleteAll()                   전체 삭제 (하나씩 SELECT 후 DELETE)
     * deleteAll(list)               넘긴 것들만 삭제
     * deleteAllById(ids)            id 목록으로 삭제
     * deleteAllInBatch()            DELETE FROM posts 한 방 ⚠️ 콜백 무시
     * deleteAllByIdInBatch(ids)     위의 id 지정 버전 ⚠️
     *
     * [기타]
     * flush()                       쌓인 변경을 DB로 밀어냄
     * findBy(example, fn)           Query By Example. 거의 안 씀
     *
     * ⚠️ getReferenceById: 없는 id 넣어도 그 자리서 안 터짐.
     *    나중에 필드 꺼낼 때 EntityNotFoundException. findById 써라.
     * ⚠️ deleteAllInBatch: 빠르지만 @PreRemove 등 생명주기 다 무시.
     *
     * 필요하면 메서드 이름만 적어도 쿼리 생성됨:
     *   List<Post> findByWriter(String writer);
     *   List<Post> findAllByOrderByCreatedAtDesc();
     */

}
