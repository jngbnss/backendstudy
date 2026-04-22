package dev.backendstudy.blog_project;

import dev.backendstudy.blog_project.dto.BoardRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service // 이 클래스는 비즈니스 로직을 담당하는 서비스야 라고 스프링에게 알림
@RequiredArgsConstructor // final이 붙은 보드 리포지터리를 생성자로 자동 주입해줌
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
}
