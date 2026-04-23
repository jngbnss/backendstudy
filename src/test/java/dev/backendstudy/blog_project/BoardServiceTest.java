package dev.backendstudy.blog_project;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.backendstudy.blog_project.dto.BoardRequestDto;
import dev.backendstudy.blog_project.dto.BoardResponseDto;
import dev.backendstudy.blog_project.dto.BoardUpdateDto;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional // 테스트 완료 후 DB를 자동으로 롤백해줘서 반복 테스트가 가능해요.
class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    @Test
    @DisplayName("게시글을 저장하면 ID가 반환되고 조회가 가능해야 한다")
    void save() {
        // given
        BoardRequestDto requestDto = new BoardRequestDto("테스트 제목", "테스트 내용", "작성자");

        // when
        Long savedId = boardService.save(requestDto);

        // then
        BoardResponseDto result = boardService.findById(savedId);
        assertThat(result.getTitle()).isEqualTo("테스트 제목");
        assertThat(result.getWriter()).isEqualTo("작성자");
    }

    @Test
    @DisplayName("전체 게시글 목록을 조회할 수 있다")
    void findAll() {
        // given
        boardService.save(new BoardRequestDto("제목1", "내용1", "작성자1"));
        boardService.save(new BoardRequestDto("제목2", "내용2", "작성자2"));

        // when
        List<BoardResponseDto> all = boardService.findAll();

        // then
        assertThat(all.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("존재하지 않는 ID로 조회하면 예외가 발생한다")
    void findById_exception() {
        // when & then
        assertThatThrownBy(() -> boardService.findById(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 게시글이 없습니다.");
    }

    @Test
    @DisplayName("게시글 수정 시 변경 감지(Dirty Checking)가 작동해야 한다")
    void update() {
        // given
        Long savedId = boardService.save(new BoardRequestDto("원래 제목", "원래 내용", "작성자"));
        BoardUpdateDto updateDto = new BoardUpdateDto("수정된 제목", "수정된 내용");

        // when
        boardService.update(savedId, updateDto);

        // then
        BoardResponseDto result = boardService.findById(savedId);
        assertThat(result.getTitle()).isEqualTo("수정된 제목");
        assertThat(result.getContent()).isEqualTo("수정된 내용");
    }
}