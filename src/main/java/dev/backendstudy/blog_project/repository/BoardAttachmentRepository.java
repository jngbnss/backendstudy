package dev.backendstudy.blog_project.repository;

import dev.backendstudy.blog_project.entity.BoardAttachment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardAttachmentRepository extends JpaRepository<BoardAttachment, Long> {
    List<BoardAttachment> findByBoardId(Long boardId);
}
