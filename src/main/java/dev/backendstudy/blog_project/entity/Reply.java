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
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_reply_id")
    private Reply parentReply;

    @OneToMany(mappedBy = "parentReply", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    private List<Reply> childReplies = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public Reply(String content, Member member, Board board, Reply parentReply) {
        this.content = content;
        this.member = member;
        if (board != null) {
            setBoard(board);
        }
        if (parentReply != null) {
            setParentReply(parentReply);
        }
    }

    public Reply(String content, Member member, Board board) {
        this(content, member, board, null);
    }

    public String getWriter() {
        return member.getUsername();
    }

    public Long getWriterId() {
        return member.getId();
    }

    public String getWriterProfileImageUrl() {
        return member.getProfileImageUrl();
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

    public void setParentReply(Reply parentReply) {
        this.parentReply = parentReply;
        if (!parentReply.getChildReplies().contains(this)) {
            parentReply.getChildReplies().add(this);
        }
    }
}
