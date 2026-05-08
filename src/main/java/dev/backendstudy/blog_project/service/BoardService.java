package dev.backendstudy.blog_project.service;

import dev.backendstudy.blog_project.dto.board.BoardRequestDto;
import dev.backendstudy.blog_project.dto.board.BoardResponseDto;
import dev.backendstudy.blog_project.dto.board.BoardUpdateDto;
import dev.backendstudy.blog_project.entity.Board;
import dev.backendstudy.blog_project.repository.BoardRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;

    @Transactional
    public Long save(BoardRequestDto requestDto) {
        Board board = new Board(
                requestDto.getTitle(),
                requestDto.getContent(),
                requestDto.getWriter()
        );
        return boardRepository.save(board).getId();
    }

    public List<BoardResponseDto> findAll() {
        return boardRepository.findAll().stream()
                .map(BoardResponseDto::new)
                .collect(Collectors.toList());
    }

    public BoardResponseDto findById(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Board not found. id=" + id));
        return new BoardResponseDto(board);
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
}
