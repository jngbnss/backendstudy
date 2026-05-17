package dev.backendstudy.blog_project.dto.board;

import dev.backendstudy.blog_project.dto.reply.ReplyResponseDto;
import dev.backendstudy.blog_project.entity.Board;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class BoardResponseDto {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private Long writerId;
    private String writerProfileImageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean modified;
    private long viewCount;
    private long likeCount;
    private long dislikeCount;
    private boolean notice;
    private boolean liked;
    private boolean disliked;
    private List<ReplyResponseDto> replies;
    private List<BoardAttachmentResponseDto> attachments;

    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.writer = board.getWriter();
        this.writerId = board.getWriterId();
        this.writerProfileImageUrl = board.getWriterProfileImageUrl();
        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getUpdatedAt();
        this.modified = board.getCreatedAt() != null
                && board.getUpdatedAt() != null
                && board.getUpdatedAt().isAfter(board.getCreatedAt());
        this.viewCount = board.getViewCount();
        this.likeCount = board.getLikeCount();
        this.dislikeCount = board.getDislikeCount();
        this.notice = board.isNotice();
        this.replies = board.getReplies().stream()
                .filter(reply -> reply.getParentReply() == null)
                .map(ReplyResponseDto::new)
                .collect(Collectors.toList());
        this.attachments = board.getAttachments().stream()
                .map(BoardAttachmentResponseDto::new)
                .collect(Collectors.toList());
    }

    public BoardResponseDto(Board board, String reactionType) {
        this(board);
        this.liked = "LIKE".equals(reactionType);
        this.disliked = "DISLIKE".equals(reactionType);
    }
}
