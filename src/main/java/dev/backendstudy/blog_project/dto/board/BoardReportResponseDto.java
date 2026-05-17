package dev.backendstudy.blog_project.dto.board;

import dev.backendstudy.blog_project.entity.BoardReport;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class BoardReportResponseDto {
    private Long id;
    private Long boardId;
    private String boardTitle;
    private Long reporterId;
    private String reporterUsername;
    private String reason;
    private boolean resolved;
    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;

    public BoardReportResponseDto(BoardReport report) {
        this.id = report.getId();
        this.boardId = report.getBoard().getId();
        this.boardTitle = report.getBoard().getTitle();
        this.reporterId = report.getReporter().getId();
        this.reporterUsername = report.getReporter().getUsername();
        this.reason = report.getReason();
        this.resolved = report.isResolved();
        this.createdAt = report.getCreatedAt();
        this.resolvedAt = report.getResolvedAt();
    }
}
