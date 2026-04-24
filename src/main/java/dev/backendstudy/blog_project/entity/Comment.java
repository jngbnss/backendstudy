package dev.backendstudy.blog_project.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public Comment(String content, String writer, Board board) {
        this.content = content;
        this.writer = writer;
        if (board != null) {
            setBoard(board); // 생성 시점에 연관관계 편의 메서드 호출
        }
    }

    /**
     * 댓글 수정 (더티 체킹 활용)
     */
    public void update(String content) {
        this.content = content;
    }

    /**
     * 연관관계 편의 메서드
     */
    public void setBoard(Board board) {
        this.board = board;
        // 게시글의 댓글 목록에 나(this)를 추가하여 양방향 정합성 유지
        if (!board.getComments().contains(this)) {
            board.getComments().add(this);
        }
    }
}