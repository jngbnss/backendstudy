package dev.backendstudy.blog_project.service;

import dev.backendstudy.blog_project.dto.reply.ReplyRequestDto;
import dev.backendstudy.blog_project.dto.reply.ReplyResponseDto;
import dev.backendstudy.blog_project.entity.Board;
import dev.backendstudy.blog_project.entity.Member;
import dev.backendstudy.blog_project.entity.Reply;
import dev.backendstudy.blog_project.repository.BoardRepository;
import dev.backendstudy.blog_project.repository.MemberRepository;
import dev.backendstudy.blog_project.repository.ReplyRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public List<ReplyResponseDto> findAllForAdmin() {
        return replyRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).stream()
                .map(ReplyResponseDto::new)
                .toList();
    }

    public Long saveReply(Long boardId, ReplyRequestDto requestDto, Member member) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found. id=" + boardId));

        Reply reply = Reply.builder()
                .content(requestDto.getContent())
                .member(member)
                .board(board)
                .build();

        return replyRepository.save(reply).getId();
    }

    public Long saveChildReply(Long boardId, Long parentReplyId, ReplyRequestDto requestDto, Member member) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found. id=" + boardId));
        Reply parentReply = replyRepository.findById(parentReplyId)
                .orElseThrow(() -> new IllegalArgumentException("Reply not found. id=" + parentReplyId));

        if (!parentReply.getBoard().getId().equals(board.getId())) {
            throw new IllegalArgumentException("Parent reply does not belong to board. boardId=" + boardId);
        }

        Reply reply = Reply.builder()
                .content(requestDto.getContent())
                .member(member)
                .board(board)
                .parentReply(parentReply)
                .build();

        return replyRepository.save(reply).getId();
    }

    public Long updateReply(Long replyId, ReplyRequestDto requestDto) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException("Reply not found. id=" + replyId));

        reply.update(requestDto.getContent());
        return replyId;
    }

    public boolean isWriter(Long replyId, Long memberId) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException("Reply not found. id=" + replyId));
        return reply.getWriterId().equals(memberId);
    }

    public boolean canManage(Long replyId, Long memberId) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException("Reply not found. id=" + replyId));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found. id=" + memberId));
        return reply.getWriterId().equals(memberId) || member.isAdmin();
    }

    public void deleteReply(Long replyId) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException("Reply not found. id=" + replyId));
        replyRepository.delete(reply);
    }
}
