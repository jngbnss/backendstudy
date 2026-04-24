package dev.backendstudy.blog_project.service;

import dev.backendstudy.blog_project.dto.BoardRequestDto;
import dev.backendstudy.blog_project.dto.BoardResponseDto;
import dev.backendstudy.blog_project.dto.BoardUpdateDto;
import dev.backendstudy.blog_project.entity.Board;
import dev.backendstudy.blog_project.repository.BoardRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service // 이 클래스는 비즈니스 로직을 담당하는 서비스야 라고 스프링에게 알림
@RequiredArgsConstructor // final이 붙은 보드 리포지터리를 생성자로 자동 주입해줌
@Transactional(readOnly=true) // 피드백 반영: 기본은 읽기 전용
public class BoardService {
    private final BoardRepository boardRepository;

    // 게시글 저장 로직
    @Transactional // DB 작업 중 에러가 나면 롤백해주는 안전장치
    public Long save(BoardRequestDto requestDto){
        Board board = new Board(
                requestDto.getTitle(),
                requestDto.getContent(),
                requestDto.getWriter()
        );
        return boardRepository.save(board).getId();
    }
    public List<BoardResponseDto> findAll(){
        return boardRepository.findAll().stream()
                .map(BoardResponseDto::new)
                .collect(Collectors.toList());
    }

    public BoardResponseDto findById(Long id){
        Board board = boardRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다. id = "+id));
        return new BoardResponseDto(board);
    }

    @Transactional // 피드백 반영: 수정 작업이므로 쓰기 권한 부여
    public void update(Long id, BoardUpdateDto requestDto){
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id = " + id));

        //2. 엔티티의 비즈니스 로직 호출
        // 리포지토리의 save()를 명시적으로 호출하지 않아도 트랜잭션이 끝날 때 DB에 반영
        board.update(requestDto.title(),requestDto.content());

    }
    @Transactional
    public void delete(Long id){
        Board board = boardRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id="+id));
        boardRepository.delete(board);
    }
}
