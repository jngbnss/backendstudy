package dev.backendstudy.blog_project.dto.board;

import dev.backendstudy.blog_project.dto.reply.ReplyResponseDto;
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
    private Long writerId;
    private List<ReplyResponseDto> replies;

    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.writer = board.getWriter();
        this.writerId = board.getWriterId();
        this.replies = board.getReplies().stream()
                .map(ReplyResponseDto::new)
                .collect(Collectors.toList());
    }
}
