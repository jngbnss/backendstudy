package dev.backendstudy.blog_project.service;

import dev.backendstudy.blog_project.dto.board.BoardResponseDto;
import dev.backendstudy.blog_project.entity.Board;
import dev.backendstudy.blog_project.entity.BoardBookmark;
import dev.backendstudy.blog_project.entity.Member;
import dev.backendstudy.blog_project.repository.BoardBookmarkRepository;
import dev.backendstudy.blog_project.repository.BoardRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardBookmarkService {
    private final BoardBookmarkRepository boardBookmarkRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public boolean toggle(Long boardId, Member member) {
        BoardBookmark bookmark = boardBookmarkRepository.findByBoardIdAndMemberId(boardId, member.getId())
                .orElse(null);
        if (bookmark != null) {
            boardBookmarkRepository.delete(bookmark);
            return false;
        }

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found. id=" + boardId));
        boardBookmarkRepository.save(new BoardBookmark(board, member));
        return true;
    }

    public boolean isBookmarked(Long boardId, Long memberId) {
        return memberId != null && boardBookmarkRepository.existsByBoardIdAndMemberId(boardId, memberId);
    }

    public List<BoardResponseDto> findMyBookmarks(Long memberId) {
        return boardBookmarkRepository.findByMemberIdOrderByIdDesc(memberId).stream()
                .map(BoardBookmark::getBoard)
                .map(BoardResponseDto::new)
                .toList();
    }
}
