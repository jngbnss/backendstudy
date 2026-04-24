package dev.backendstudy.blog_project.service;

import dev.backendstudy.blog_project.dto.CommentRequestDto;
import dev.backendstudy.blog_project.entity.Board;
import dev.backendstudy.blog_project.entity.Comment;
import dev.backendstudy.blog_project.repository.BoardRepository;
import dev.backendstudy.blog_project.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    // 생성 (기존 유지)
    public Long saveComment(Long boardId, CommentRequestDto requestDto){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(()->new IllegalArgumentException("해당 게시글이 없습니다. id ="+boardId));

        Comment comment = Comment.builder()
                .content(requestDto.getContent())
                .writer(requestDto.getWriter())
                .board(board)
                .build();

        return commentRepository.save(comment).getId();
    }

    // 수정
    public Long updateComment(Long commentId, CommentRequestDto requestDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. id=" + commentId));

        // Comment 엔티티에 update 메서드 추가 필요 (아래 엔티티 섹션 참고)
        comment.update(requestDto.getContent());
        return commentId;
    }

    // 삭제
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. id=" + commentId));
        commentRepository.delete(comment);
    }



}
