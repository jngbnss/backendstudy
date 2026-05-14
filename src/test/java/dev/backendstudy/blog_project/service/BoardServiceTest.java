package dev.backendstudy.blog_project.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.backendstudy.blog_project.dto.board.BoardRequestDto;
import dev.backendstudy.blog_project.dto.board.BoardResponseDto;
import dev.backendstudy.blog_project.dto.board.BoardUpdateDto;
import dev.backendstudy.blog_project.entity.Member;
import dev.backendstudy.blog_project.repository.MemberRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class BoardServiceTest {
    @Autowired
    private BoardService boardService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("save board")
    void save() {
        Member member = memberRepository.save(new Member("writer", "writerId", "password"));
        BoardRequestDto requestDto = new BoardRequestDto("test title", "test content", null);

        Long savedId = boardService.save(requestDto, member);

        BoardResponseDto result = boardService.findById(savedId);
        assertThat(result.getTitle()).isEqualTo("test title");
        assertThat(result.getWriter()).isEqualTo("writer");
        assertThat(result.getWriterId()).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("find all boards")
    void findAll() {
        Member member = memberRepository.save(new Member("writer", "writerId", "password"));
        boardService.save(new BoardRequestDto("title1", "content1", null), member);
        boardService.save(new BoardRequestDto("title2", "content2", null), member);

        List<BoardResponseDto> all = boardService.findAll();

        assertThat(all.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("find by id exception")
    void findById_exception() {
        assertThatThrownBy(() -> boardService.findById(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Board not found.");
    }

    @Test
    @DisplayName("update board")
    void update() {
        Member member = memberRepository.save(new Member("writer", "writerId", "password"));
        Long savedId = boardService.save(new BoardRequestDto("old title", "old content", null), member);
        BoardUpdateDto updateDto = new BoardUpdateDto("updated title", "updated content");

        boardService.update(savedId, updateDto);

        BoardResponseDto result = boardService.findById(savedId);
        assertThat(result.getTitle()).isEqualTo("updated title");
        assertThat(result.getContent()).isEqualTo("updated content");
    }

    @Test
    @DisplayName("delete board")
    void delete() {
        Member member = memberRepository.save(new Member("writer", "writerId", "password"));
        BoardRequestDto requestDto = new BoardRequestDto("delete title", "content", null);
        Long savedId = boardService.save(requestDto, member);

        boardService.delete(savedId);

        assertThatThrownBy(() -> boardService.findById(savedId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Board not found.");
    }
}
