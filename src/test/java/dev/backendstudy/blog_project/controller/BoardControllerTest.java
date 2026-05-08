package dev.backendstudy.blog_project.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.backendstudy.blog_project.dto.board.BoardRequestDto;
import dev.backendstudy.blog_project.dto.board.BoardUpdateDto;
import dev.backendstudy.blog_project.entity.Board;
import dev.backendstudy.blog_project.repository.BoardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BoardControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BoardRepository boardRepository;

    @Test
    @DisplayName("create board")
    void createBoard() throws Exception {
        BoardRequestDto requestDto = new BoardRequestDto("controller title", "controller content", "writer");
        String json = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("get all boards")
    void getAllBoards() throws Exception {
        boardRepository.save(new Board("title1", "content1", "writer"));

        mockMvc.perform(get("/api/boards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("title1"));
    }

    @Test
    @DisplayName("get board")
    void getBoard() throws Exception {
        Board saved = boardRepository.save(new Board("read title", "content", "writer"));

        mockMvc.perform(get("/api/boards/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("read title"))
                .andExpect(jsonPath("$.content").value("content"));
    }

    @Test
    @DisplayName("update board")
    void updateBoard() throws Exception {
        Board saved = boardRepository.save(new Board("old title", "content", "writer"));
        BoardUpdateDto updateDto = new BoardUpdateDto("updated title", "updated content");
        String json = objectMapper.writeValueAsString(updateDto);

        mockMvc.perform(put("/api/boards/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("delete board")
    void deleteBoard() throws Exception {
        Board saved = boardRepository.save(new Board("delete title", "content", "writer"));

        mockMvc.perform(delete("/api/boards/" + saved.getId()))
                .andExpect(status().isOk());

        boolean exists = boardRepository.existsById(saved.getId());
        assertThat(exists).isFalse();
    }
}
