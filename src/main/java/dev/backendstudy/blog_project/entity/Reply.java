package dev.backendstudy.blog_project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public Reply(String content, Member member, Board board) {
        this.content = content;
        this.member = member;
        if (board != null) {
            setBoard(board);
        }
    }

    public String getWriter() {
        return member.getUsername();
    }

    public Long getWriterId() {
        return member.getId();
    }

    public void update(String content) {
        this.content = content;
    }

    public void setBoard(Board board) {
        this.board = board;
        if (!board.getReplies().contains(this)) {
            board.getReplies().add(this);
        }
    }
}
