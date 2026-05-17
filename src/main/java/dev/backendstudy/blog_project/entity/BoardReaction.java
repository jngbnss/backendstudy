package dev.backendstudy.blog_project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"board_id", "member_id"}))
public class BoardReaction {
    public static final String LIKE = "LIKE";
    public static final String DISLIKE = "DISLIKE";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false, length = 20)
    private String reactionType;

    public BoardReaction(Board board, Member member, String reactionType) {
        this.board = board;
        this.member = member;
        this.reactionType = reactionType;
    }

    public boolean isLike() {
        return LIKE.equals(reactionType);
    }

    public boolean isDislike() {
        return DISLIKE.equals(reactionType);
    }

    public void changeTo(String reactionType) {
        this.reactionType = reactionType;
    }
}
