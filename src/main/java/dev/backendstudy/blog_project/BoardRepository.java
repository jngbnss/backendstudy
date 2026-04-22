package dev.backendstudy.blog_project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board,Long> {
}
/**
 * 기능,메서드,설명,SQL 비유
 * 저장/수정,save(entity),"새 글을 저장하거나, ID가 있으면 내용을 수정합니다.",INSERT / UPDATE
 * 단건 조회,findById(id),ID(PK)로 특정 게시글 하나를 찾습니다.,SELECT ... WHERE id = ?
 * 전체 조회,findAll(),DB에 있는 모든 게시글을 리스트로 가져옵니다.,SELECT * FROM board
 * 삭제,deleteById(id),ID로 특정 게시글을 삭제합니다.,DELETE FROM board WHERE id = ?
 * 개수 확인,count(),전체 게시글이 몇 개인지 알려줍니다.,SELECT COUNT(*) ...
 */
