package dev.backendstudy.blog_project.repository;

import dev.backendstudy.blog_project.entity.BoardBookmark;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardBookmarkRepository extends JpaRepository<BoardBookmark, Long> {
    Optional<BoardBookmark> findByBoardIdAndMemberId(Long boardId, Long memberId);
    boolean existsByBoardIdAndMemberId(Long boardId, Long memberId);
    List<BoardBookmark> findByMemberIdOrderByIdDesc(Long memberId);
}
