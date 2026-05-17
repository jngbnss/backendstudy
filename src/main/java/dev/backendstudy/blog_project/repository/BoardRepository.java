package dev.backendstudy.blog_project.repository;

import dev.backendstudy.blog_project.entity.Board;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
            String titleKeyword,
            String contentKeyword,
            Pageable pageable
    );

    Page<Board> findByMemberId(Long memberId, Pageable pageable);

    Page<Board> findByMemberIdAndTitleContainingIgnoreCaseOrMemberIdAndContentContainingIgnoreCase(
            Long titleMemberId,
            String titleKeyword,
            Long contentMemberId,
            String contentKeyword,
            Pageable pageable
    );

    @Modifying
    @Query("""
            update Board b
            set b.createdAt = :createdAt,
                b.updatedAt = :createdAt,
                b.viewCount = :viewCount,
                b.likeCount = :likeCount,
                b.dislikeCount = :dislikeCount
            where b.id = :id
            """)
    void updateDummyFields(
            @Param("id") Long id,
            @Param("createdAt") LocalDateTime createdAt,
            @Param("viewCount") long viewCount,
            @Param("likeCount") long likeCount,
            @Param("dislikeCount") long dislikeCount
    );
}
