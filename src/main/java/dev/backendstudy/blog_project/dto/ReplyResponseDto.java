package dev.backendstudy.blog_project.dto;

import dev.backendstudy.blog_project.entity.Reply;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ReplyResponseDto {
    private Long id;
    private String content;
    private String writer;
    private LocalDateTime createdAt;

    public ReplyResponseDto(Reply reply) {
        this.id = reply.getId();
        this.content = reply.getContent();
        this.writer = reply.getWriter();
        this.createdAt = reply.getCreatedAt();
    }
}