package dev.backendstudy.blog_project.controller;

import dev.backendstudy.blog_project.dto.reply.ReplyRequestDto;
import dev.backendstudy.blog_project.entity.Member;
import dev.backendstudy.blog_project.service.MemberService;
import dev.backendstudy.blog_project.service.ReplyService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReplyController {
    private final ReplyService replyService;
    private final MemberService memberService;

    @PostMapping("/boards/{boardId}/replies")
    public ResponseEntity<Long> createReply(
            @PathVariable Long boardId,
            @RequestBody ReplyRequestDto requestDto,
            HttpSession session
    ) {
        Long loginMemberId = (Long) session.getAttribute("loginMemberId");

        if (loginMemberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Member member = memberService.findMember(loginMemberId);

        return ResponseEntity.status(HttpStatus.CREATED).body(replyService.saveReply(boardId, requestDto, member));
    }

    @PutMapping("/replies/{replyId}")
    public ResponseEntity<Long> updateReply(
            @PathVariable Long replyId,
            @RequestBody ReplyRequestDto requestDto,
            HttpSession session
    ) {
        Long loginMemberId = (Long) session.getAttribute("loginMemberId");

        if (loginMemberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!replyService.isWriter(replyId, loginMemberId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(replyService.updateReply(replyId, requestDto));
    }

    @DeleteMapping("/replies/{replyId}")
    public ResponseEntity<Void> deleteReply(@PathVariable Long replyId, HttpSession session) {
        Long loginMemberId = (Long) session.getAttribute("loginMemberId");

        if (loginMemberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!replyService.isWriter(replyId, loginMemberId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        replyService.deleteReply(replyId);
        return ResponseEntity.noContent().build();
    }
}
