package dev.backendstudy.blog_project.service;

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
@Transactional
class BoardServiceTest {
    @Autowired
    private BoardService boardService;

    @Test
    @DisplayName("save board")
    void save() {
        BoardRequestDto requestDto = new BoardRequestDto("test title", "test content", "writer");

        Long savedId = boardService.save(requestDto);

        BoardResponseDto result = boardService.findById(savedId);
        assertThat(result.getTitle()).isEqualTo("test title");
        assertThat(result.getWriter()).isEqualTo("writer");
    }

    @Test
    @DisplayName("find all boards")
    void findAll() {
        boardService.save(new BoardRequestDto("title1", "content1", "writer"));
        boardService.save(new BoardRequestDto("title2", "content2", "writer"));

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
        Long savedId = boardService.save(new BoardRequestDto("old title", "old content", "writer"));
        BoardUpdateDto updateDto = new BoardUpdateDto("updated title", "updated content");

        boardService.update(savedId, updateDto);

        BoardResponseDto result = boardService.findById(savedId);
        assertThat(result.getTitle()).isEqualTo("updated title");
        assertThat(result.getContent()).isEqualTo("updated content");
    }

    @Test
    @DisplayName("delete board")
    void delete() {
        BoardRequestDto requestDto = new BoardRequestDto("delete title", "content", "writer");
        Long savedId = boardService.save(requestDto);

        boardService.delete(savedId);

        assertThatThrownBy(() -> boardService.findById(savedId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Board not found.");
    }
}