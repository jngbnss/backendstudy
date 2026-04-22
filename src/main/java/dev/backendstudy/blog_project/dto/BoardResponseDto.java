package dev.backendstudy.blog_project.dto;

import dev.backendstudy.blog_project.Board;
import lombok.Getter;

@Getter
public class BoardResponseDto {
    private Long id;
    private String title;
    private String content;
    private String writer;

    public BoardResponseDto(Board board){
        this.id = board.getId();
        this.title = board.getTitle();
        this.content =board.getContent();
        this.writer = board.getWriter();
    }
}
