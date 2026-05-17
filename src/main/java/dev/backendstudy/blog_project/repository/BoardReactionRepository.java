package dev.backendstudy.blog_project.repository;

import dev.backendstudy.blog_project.entity.BoardReaction;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardReactionRepository extends JpaRepository<BoardReaction, Long> {
    Optional<BoardReaction> findByBoardIdAndMemberId(Long boardId, Long memberId);
}
