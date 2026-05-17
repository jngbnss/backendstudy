package dev.backendstudy.blog_project.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import dev.backendstudy.blog_project.dto.reply.ReplyRequestDto;
import dev.backendstudy.blog_project.entity.Board;
import dev.backendstudy.blog_project.entity.Member;
import dev.backendstudy.blog_project.entity.Reply;
import dev.backendstudy.blog_project.repository.BoardRepository;
import dev.backendstudy.blog_project.repository.MemberRepository;
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

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private NotificationService notificationService;

    @Test
    @DisplayName("save reply")
    void saveReply_success() {
        Long boardId = 1L;
        Member member = new Member("reply writer", "replyWriter", "password");
        Board board = new Board("title", "content", member);
        ReplyRequestDto requestDto = new ReplyRequestDto("reply content", null);
        Reply reply = Reply.builder()
                .content(requestDto.getContent())
                .member(member)
                .board(board)
                .build();
        ReflectionTestUtils.setField(reply, "id", 100L);

        given(boardRepository.findById(boardId)).willReturn(Optional.of(board));
        given(replyRepository.save(any(Reply.class))).willReturn(reply);

        Long savedId = replyService.saveReply(boardId, requestDto, member);

        assertThat(savedId).isEqualTo(100L);
        verify(replyRepository).save(any(Reply.class));
    }

    @Test
    @DisplayName("update reply")
    void updateReply_success() {
        Long replyId = 1L;
        Member member = new Member("writer", "writerId", "password");
        Reply reply = Reply.builder().content("old content").member(member).build();
        ReplyRequestDto updateDto = new ReplyRequestDto("updated content", null);

        given(replyRepository.findById(replyId)).willReturn(Optional.of(reply));

        Long updatedId = replyService.updateReply(replyId, updateDto);

        assertThat(updatedId).isEqualTo(replyId);
        assertThat(reply.getContent()).isEqualTo("updated content");
    }

    @Test
    @DisplayName("delete reply")
    void deleteReply_success() {
        Long replyId = 1L;
        Member member = new Member("writer", "writerId", "password");
        Reply reply = Reply.builder().content("delete content").member(member).build();

        given(replyRepository.findById(replyId)).willReturn(Optional.of(reply));

        replyService.deleteReply(replyId);

        verify(replyRepository).delete(reply);
    }
}
