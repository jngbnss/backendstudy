package dev.backendstudy.blog_project.dto;



import dev.backendstudy.blog_project.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private Long id;
    private String content;
    private String writer;
    private LocalDateTime createdAt;

    // Entity -> DTO 변환을 위한 생성자
    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.writer = comment.getWriter();
        this.createdAt = comment.getCreatedAt();
    }
}