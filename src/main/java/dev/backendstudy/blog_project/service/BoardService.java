package dev.backendstudy.blog_project.service;

import dev.backendstudy.blog_project.dto.board.BoardRequestDto;
import dev.backendstudy.blog_project.dto.board.BoardResponseDto;
import dev.backendstudy.blog_project.dto.board.BoardUpdateDto;
import dev.backendstudy.blog_project.entity.Board;
import dev.backendstudy.blog_project.entity.BoardReaction;
import dev.backendstudy.blog_project.entity.Member;
import dev.backendstudy.blog_project.repository.BoardRepository;
import dev.backendstudy.blog_project.repository.BoardReactionRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardReactionRepository boardReactionRepository;

    @Transactional
    public Long save(BoardRequestDto requestDto, Member member) {
        Board board = new Board(
                requestDto.getTitle(),
                requestDto.getContent(),
                member,
                member.isAdmin()
        );
        return boardRepository.save(board).getId();
    }

    public List<BoardResponseDto> findAll() {
        return boardRepository.findAll().stream()
                .map(BoardResponseDto::new)
                .collect(Collectors.toList());
    }

    public Page<BoardResponseDto> findPage(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) {
            return boardRepository.findAll(pageable)
                    .map(BoardResponseDto::new);
        }

        String trimmedKeyword = keyword.trim();
        return boardRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
                        trimmedKeyword,
                        trimmedKeyword,
                        pageable
                )
                .map(BoardResponseDto::new);
    }

    public Page<BoardResponseDto> findMyPage(Long memberId, String keyword, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) {
            return boardRepository.findByMemberId(memberId, pageable)
                    .map(BoardResponseDto::new);
        }

        String trimmedKeyword = keyword.trim();
        return boardRepository.findByMemberIdAndTitleContainingIgnoreCaseOrMemberIdAndContentContainingIgnoreCase(
                        memberId,
                        trimmedKeyword,
                        memberId,
                        trimmedKeyword,
                        pageable
                )
                .map(BoardResponseDto::new);
    }

    public BoardResponseDto findById(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Board not found. id=" + id));
        return new BoardResponseDto(board);
    }

    @Transactional
    public BoardResponseDto findByIdForView(Long id, Long loginMemberId) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Board not found. id=" + id));

        if (loginMemberId == null || !board.getWriterId().equals(loginMemberId)) {
            board.increaseViewCount();
        }

        String reactionType = null;
        if (loginMemberId != null) {
            reactionType = boardReactionRepository.findByBoardIdAndMemberId(id, loginMemberId)
                    .map(BoardReaction::getReactionType)
                    .orElse(null);
        }

        return new BoardResponseDto(board, reactionType);
    }

    public boolean isWriter(Long id, Long memberId) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Board not found. id=" + id));
        return board.getWriterId().equals(memberId);
    }

    @Transactional
    public void update(Long id, BoardUpdateDto requestDto) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Board not found. id=" + id));
        board.update(requestDto.title(), requestDto.content());
    }

    @Transactional
    public void delete(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Board not found. id=" + id));
        boardRepository.delete(board);
    }

    @Transactional
    public void react(Long boardId, Member member, String reactionType) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found. id=" + boardId));

        BoardReaction reaction = boardReactionRepository.findByBoardIdAndMemberId(boardId, member.getId())
                .orElse(null);

        if (reaction == null) {
            boardReactionRepository.save(new BoardReaction(board, member, reactionType));
            increaseReactionCount(board, reactionType);
            return;
        }

        if (reaction.getReactionType().equals(reactionType)) {
            decreaseReactionCount(board, reactionType);
            boardReactionRepository.delete(reaction);
            return;
        }

        decreaseReactionCount(board, reaction.getReactionType());
        reaction.changeTo(reactionType);
        increaseReactionCount(board, reactionType);
    }

    private void increaseReactionCount(Board board, String reactionType) {
        if (BoardReaction.LIKE.equals(reactionType)) {
            board.increaseLikeCount();
        } else if (BoardReaction.DISLIKE.equals(reactionType)) {
            board.increaseDislikeCount();
        }
    }

    private void decreaseReactionCount(Board board, String reactionType) {
        if (BoardReaction.LIKE.equals(reactionType)) {
            board.decreaseLikeCount();
        } else if (BoardReaction.DISLIKE.equals(reactionType)) {
            board.decreaseDislikeCount();
        }
    }
}
