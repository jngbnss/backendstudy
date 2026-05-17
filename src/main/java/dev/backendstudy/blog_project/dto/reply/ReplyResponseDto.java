package dev.backendstudy.blog_project.dto.reply;

import dev.backendstudy.blog_project.entity.Reply;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class ReplyResponseDto {
    private Long id;
    private String content;
    private String writer;
    private Long writerId;
    private Long boardId;
    private String boardTitle;
    private Long parentReplyId;
    private String writerProfileImageUrl;
    private LocalDateTime createdAt;
    private List<ReplyResponseDto> childReplies;

    public ReplyResponseDto(Reply reply) {
        this.id = reply.getId();
        this.content = reply.getContent();
        this.writer = reply.getWriter();
        this.writerId = reply.getWriterId();
        this.boardId = reply.getBoard() == null ? null : reply.getBoard().getId();
        this.boardTitle = reply.getBoard() == null ? null : reply.getBoard().getTitle();
        this.parentReplyId = reply.getParentReply() == null ? null : reply.getParentReply().getId();
        this.writerProfileImageUrl = reply.getWriterProfileImageUrl();
        this.createdAt = reply.getCreatedAt();
        this.childReplies = reply.getChildReplies().stream()
                .map(ReplyResponseDto::new)
                .collect(Collectors.toList());
    }
}
