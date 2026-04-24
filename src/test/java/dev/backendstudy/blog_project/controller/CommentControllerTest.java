package dev.backendstudy.blog_project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.backendstudy.blog_project.dto.CommentRequestDto;
import dev.backendstudy.blog_project.service.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// 패키지 경로를 주의 깊게 확인하세요! (Spring Boot 3.4+)
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // 1. 기존 @MockBean 대신 사용합니다.
    // 2. 클래스 내부 필드로 선언해야 합니다.
    @MockitoBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("댓글 생성 API 테스트")
    void createComment() throws Exception {
        // given
        Long boardId = 1L;
        CommentRequestDto requestDto = new CommentRequestDto("댓글 내용", "작성자");
        given(commentService.saveComment(any(), any())).willReturn(10L);

        // when & then
        mockMvc.perform(post("/api/boards/{boardId}/comments", boardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("10"));
    }

    @Test
    @DisplayName("댓글 수정 API 테스트")
    void updateComment() throws Exception {
        // given
        Long commentId = 1L;
        CommentRequestDto requestDto = new CommentRequestDto("수정된 내용", "작성자");
        given(commentService.updateComment(any(), any())).willReturn(commentId);

        // when & then
        mockMvc.perform(put("/api/comments/{commentId}", commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    @DisplayName("댓글 삭제 API 테스트")
    void deleteComment() throws Exception {
        // given
        Long commentId = 1L;

        // when & then
        mockMvc.perform(delete("/api/comments/{commentId}", commentId))
                .andExpect(status().isNoContent());
    }
}