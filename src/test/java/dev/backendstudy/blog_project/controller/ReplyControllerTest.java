package dev.backendstudy.blog_project.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.backendstudy.blog_project.dto.reply.ReplyRequestDto;
import dev.backendstudy.blog_project.entity.Member;
import dev.backendstudy.blog_project.service.MemberService;
import dev.backendstudy.blog_project.service.ReplyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ReplyController.class)
class ReplyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReplyService replyService;

    @MockitoBean
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("create reply")
    void createReply() throws Exception {
        Long boardId = 1L;
        Long memberId = 1L;
        Member member = new Member("writer", "writerId", "password");
        ReplyRequestDto requestDto = new ReplyRequestDto("reply content", "writer");
        given(memberService.findMember(memberId)).willReturn(member);
        given(replyService.saveReply(eq(boardId), any(), eq(member))).willReturn(10L);

        mockMvc.perform(post("/api/boards/{boardId}/replies", boardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .sessionAttr("loginMemberId", memberId)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("10"));
    }

    @Test
    @DisplayName("update reply")
    void updateReply() throws Exception {
        Long replyId = 1L;
        Long memberId = 1L;
        ReplyRequestDto requestDto = new ReplyRequestDto("updated content", "writer");
        given(replyService.isWriter(replyId, memberId)).willReturn(true);
        given(replyService.updateReply(any(), any())).willReturn(replyId);

        mockMvc.perform(put("/api/replies/{replyId}", replyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .sessionAttr("loginMemberId", memberId)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    @DisplayName("delete reply")
    void deleteReply() throws Exception {
        Long replyId = 1L;
        Long memberId = 1L;
        given(replyService.isWriter(replyId, memberId)).willReturn(true);

        mockMvc.perform(delete("/api/replies/{replyId}", replyId)
                        .sessionAttr("loginMemberId", memberId))
                .andExpect(status().isNoContent());
    }
}
