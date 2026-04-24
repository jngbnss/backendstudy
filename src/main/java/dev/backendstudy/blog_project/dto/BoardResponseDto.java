package dev.backendstudy.blog_project.dto;

import dev.backendstudy.blog_project.entity.Board;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class BoardResponseDto {
    private Long id;
    private String title;
    private String content;
    private String writer;

    // 1. 타입을 CommentRequestDto에서 CommentResponseDto로 변경해야 합니다.
    private List<CommentResponseDto> comments;

    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.writer = board.getWriter();

        // 2. 이제 타입이 일치하므로 정상적으로 변환됩니다.
        this.comments = board.getComments().stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }
}