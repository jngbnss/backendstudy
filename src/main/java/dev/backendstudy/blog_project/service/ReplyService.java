package dev.backendstudy.blog_project.service;

import dev.backendstudy.blog_project.dto.reply.ReplyRequestDto;
import dev.backendstudy.blog_project.entity.Board;
import dev.backendstudy.blog_project.entity.Reply;
import dev.backendstudy.blog_project.repository.BoardRepository;
import dev.backendstudy.blog_project.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;

    public Long saveReply(Long boardId, ReplyRequestDto requestDto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found. id=" + boardId));

        Reply reply = Reply.builder()
                .content(requestDto.getContent())
                .writer(requestDto.getWriter())
                .board(board)
                .build();

        return replyRepository.save(reply).getId();
    }

    public Long updateReply(Long replyId, ReplyRequestDto requestDto) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException("Reply not found. id=" + replyId));

        reply.update(requestDto.getContent());
        return replyId;
    }

    public void deleteReply(Long replyId) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException("Reply not found. id=" + replyId));
        replyRepository.delete(reply);
    }
}
