package dev.backendstudy.blog_project;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.backendstudy.blog_project.dto.BoardRequestDto;
import dev.backendstudy.blog_project.dto.BoardUpdateDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc // MockMvc를 생성하고 구성해주는 어노테이션
@Transactional
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // 객체를 JSON 문자열로 변환하기 위해 필요

    @Autowired
    private BoardRepository boardRepository;

    @Test
    @DisplayName("POST /api/boards: 게시글 생성에 성공한다")
    void createBoard() throws Exception {
        // given
        BoardRequestDto requestDto = new BoardRequestDto("컨트롤러 제목", "컨트롤러 내용", "작성자");
        String json = objectMapper.writeValueAsString(requestDto); // DTO를 JSON으로 변환

        // when & then
        mockMvc.perform(post("/api/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated()); // 201 Created 응답 확인
    }

    @Test
    @DisplayName("GET /api/boards: 전체 게시글을 조회한다")
    void getAllBoards() throws Exception {
        // given
        boardRepository.save(new Board("제목1", "내용1", "작성자1"));

        // when & then
        mockMvc.perform(get("/api/boards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("제목1"));
    }

    @Test
    @DisplayName("GET /api/boards/{id}: 특정 게시글 조회에 성공한다")
    void getBoard() throws Exception {
        // given
        Board saved = boardRepository.save(new Board("조회 제목", "내용", "작성자"));

        // when & then
        mockMvc.perform(get("/api/boards/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("조회 제목"))
                .andExpect(jsonPath("$.content").value("내용"));
    }

    @Test
    @DisplayName("PUT /api/boards/{id}: 게시글 수정에 성공한다")
    void updateBoard() throws Exception {
        // given
        Board saved = boardRepository.save(new Board("수정 전", "내용", "작성자"));
        BoardUpdateDto updateDto = new BoardUpdateDto("수정 후", "새 내용");
        String json = objectMapper.writeValueAsString(updateDto);

        // when & then
        mockMvc.perform(put("/api/boards/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }
}