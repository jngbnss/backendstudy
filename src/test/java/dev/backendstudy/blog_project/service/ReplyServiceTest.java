package dev.backendstudy.blog_project.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import dev.backendstudy.blog_project.dto.reply.ReplyRequestDto;
import dev.backendstudy.blog_project.entity.Board;
import dev.backendstudy.blog_project.entity.Reply;
import dev.backendstudy.blog_project.repository.BoardRepository;
import dev.backendstudy.blog_project.repository.ReplyRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ReplyServiceTest {
    @InjectMocks
    private ReplyService replyService;

    @Mock
    private ReplyRepository replyRepository;

    @Mock
    private BoardRepository boardRepository;

    @Test
    @DisplayName("save reply")
    void saveReply_success() {
        Long boardId = 1L;
        Board board = new Board("title", "content", "writer");
        ReplyRequestDto requestDto = new ReplyRequestDto("reply content", "reply writer");
        Reply reply = Reply.builder()
                .content(requestDto.getContent())
                .writer(requestDto.getWriter())
                .board(board)
                .build();
        ReflectionTestUtils.setField(reply, "id", 100L);

        given(boardRepository.findById(boardId)).willReturn(Optional.of(board));
        given(replyRepository.save(any(Reply.class))).willReturn(reply);

        Long savedId = replyService.saveReply(boardId, requestDto);

        assertThat(savedId).isEqualTo(100L);
        verify(replyRepository).save(any(Reply.class));
    }

    @Test
    @DisplayName("update reply")
    void updateReply_success() {
        Long replyId = 1L;
        Reply reply = Reply.builder().content("old content").writer("writer").build();
        ReplyRequestDto updateDto = new ReplyRequestDto("updated content", "writer");

        given(replyRepository.findById(replyId)).willReturn(Optional.of(reply));

        Long updatedId = replyService.updateReply(replyId, updateDto);

        assertThat(updatedId).isEqualTo(replyId);
        assertThat(reply.getContent()).isEqualTo("updated content");
    }

    @Test
    @DisplayName("delete reply")
    void deleteReply_success() {
        Long replyId = 1L;
        Reply reply = Reply.builder().content("delete content").writer("writer").build();

        given(replyRepository.findById(replyId)).willReturn(Optional.of(reply));

        replyService.deleteReply(replyId);

        verify(replyRepository).delete(reply);
    }
}
