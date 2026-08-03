package dev.backendstudy.board.domain.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 게시글_작성하면_201과_목록에_반영된다() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of(
                "title", "첫 번째 게시글",
                "content", "게시글 내용입니다.",
                "author", "문종빈"
        ));

        // 작성 → 201 Created
        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("첫 번째 게시글"))
                .andExpect(jsonPath("$.author").value("문종빈"));

        // 목록 → 방금 글이 보임
        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("첫 번째 게시글"));
    }

    @Test
    void 제목이_비면_400을_반환한다() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of(
                "title", "",
                "content", "내용",
                "author", "문종빈"
        ));

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    void 없는_게시글_조회하면_404를_반환한다() throws Exception {
        mockMvc.perform(get("/api/posts/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("POST_NOT_FOUND"));
    }
}
