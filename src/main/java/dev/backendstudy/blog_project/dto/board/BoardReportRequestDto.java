package dev.backendstudy.blog_project.dto.board;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardReportRequestDto {
    private String reason;

    public BoardReportRequestDto(String reason) {
        this.reason = reason;
    }
}
