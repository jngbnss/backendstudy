package dev.backendstudy.blog_project.service;

import dev.backendstudy.blog_project.dto.CommentRequestDto;
import dev.backendstudy.blog_project.entity.Board;
import dev.backendstudy.blog_project.entity.Comment;
import dev.backendstudy.blog_project.repository.BoardRepository;
import dev.backendstudy.blog_project.repository.CommentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class) // Mockito 가짜 객체 사용 설정
class CommentServiceTest {

    @InjectMocks
    private CommentService commentService; // 가짜 객체를 주입받을 대상

    @Mock
    private CommentRepository commentRepository; // 가짜 객체

    @Mock
    private BoardRepository boardRepository;

    @Test
    @DisplayName("댓글 저장 성공")
    void saveComment_success() {
        // given
        Long boardId = 1L;
        Board board = new Board("제목", "내용", "작성자");
        CommentRequestDto requestDto = new CommentRequestDto("댓글내용", "댓글작성자");

        Comment comment = Comment.builder()
                .content(requestDto.getContent())
                .writer(requestDto.getWriter())
                .board(board)
                .build();
        // ID는 DB가 넣어주므로 Reflection으로 강제 주입 (테스트용)
        ReflectionTestUtils.setField(comment, "id", 100L);

        given(boardRepository.findById(boardId)).willReturn(Optional.of(board));
        given(commentRepository.save(any(Comment.class))).willReturn(comment);

        // when
        Long savedId = commentService.saveComment(boardId, requestDto);

        // then
        assertThat(savedId).isEqualTo(100L);
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    @DisplayName("댓글 수정 성공 - 더티 체킹 확인")
    void updateComment_success() {
        // given
        Long commentId = 1L;
        Comment comment = Comment.builder().content("기존내용").writer("작성자").build();
        CommentRequestDto updateDto = new CommentRequestDto("수정된내용", "작성자");

        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

        // when
        Long updatedId = commentService.updateComment(commentId, updateDto);

        // then
        assertThat(updatedId).isEqualTo(commentId);
        assertThat(comment.getContent()).isEqualTo("수정된내용"); // 객체 상태가 바뀌었는지 확인
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void deleteComment_success() {
        // given
        Long commentId = 1L;
        Comment comment = Comment.builder().content("삭제될 내용").writer("작성자").build();

        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

        // when
        commentService.deleteComment(commentId);

        // then
        verify(commentRepository).delete(comment); // delete 메서드가 호출되었는지 확인
    }
}