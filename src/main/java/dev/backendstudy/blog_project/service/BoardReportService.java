package dev.backendstudy.blog_project.service;

import dev.backendstudy.blog_project.dto.board.BoardReportRequestDto;
import dev.backendstudy.blog_project.dto.board.BoardReportResponseDto;
import dev.backendstudy.blog_project.entity.Board;
import dev.backendstudy.blog_project.entity.BoardReport;
import dev.backendstudy.blog_project.entity.Member;
import dev.backendstudy.blog_project.repository.BoardReportRepository;
import dev.backendstudy.blog_project.repository.BoardRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardReportService {
    private final BoardReportRepository boardReportRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public Long report(Long boardId, BoardReportRequestDto requestDto, Member reporter) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found. id=" + boardId));

        String reason = requestDto.getReason();
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Report reason is required.");
        }

        BoardReport report = new BoardReport(board, reporter, reason.trim());
        return boardReportRepository.save(report).getId();
    }

    public List<BoardReportResponseDto> findAll() {
        return boardReportRepository.findAll().stream()
                .map(BoardReportResponseDto::new)
                .toList();
    }

    @Transactional
    public void resolve(Long reportId) {
        BoardReport report = boardReportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Report not found. id=" + reportId));
        report.resolve();
    }
}
