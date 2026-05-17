package dev.backendstudy.blog_project.controller;

import dev.backendstudy.blog_project.dto.board.BoardReportResponseDto;
import dev.backendstudy.blog_project.dto.board.BoardResponseDto;
import dev.backendstudy.blog_project.dto.member.MemberResponseDto;
import dev.backendstudy.blog_project.dto.reply.ReplyResponseDto;
import dev.backendstudy.blog_project.entity.MemberRole;
import dev.backendstudy.blog_project.service.BoardReportService;
import dev.backendstudy.blog_project.service.BoardService;
import dev.backendstudy.blog_project.service.MemberService;
import dev.backendstudy.blog_project.service.ReplyService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    private final MemberService memberService;
    private final BoardService boardService;
    private final BoardReportService boardReportService;
    private final ReplyService replyService;

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponseDto>> getMembers(
            @RequestParam(defaultValue = "false") boolean includeDeleted,
            HttpSession session
    ) {
        if (!isAdmin(session)) {
            return forbiddenOrUnauthorized(session);
        }

        if (includeDeleted) {
            return ResponseEntity.ok(memberService.findAllMembers());
        }
        return ResponseEntity.ok(memberService.findAllActiveMembers());
    }

    @GetMapping("/members/deleted")
    public ResponseEntity<List<MemberResponseDto>> getDeletedMembers(HttpSession session) {
        if (!isAdmin(session)) {
            return forbiddenOrUnauthorized(session);
        }

        return ResponseEntity.ok(memberService.findDeletedMembers());
    }

    @PatchMapping("/members/{memberId}/role")
    public ResponseEntity<Void> updateMemberRole(
            @PathVariable Long memberId,
            @RequestParam MemberRole role,
            HttpSession session
    ) {
        if (!isAdmin(session)) {
            return forbiddenOrUnauthorized(session);
        }

        memberService.updateRole(memberId, role);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/{memberId}")
    public ResponseEntity<Void> withdrawMember(@PathVariable Long memberId, HttpSession session) {
        if (!isAdmin(session)) {
            return forbiddenOrUnauthorized(session);
        }

        memberService.withdraw(memberId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reports")
    public ResponseEntity<List<BoardReportResponseDto>> getReports(HttpSession session) {
        if (!isAdmin(session)) {
            return forbiddenOrUnauthorized(session);
        }

        return ResponseEntity.ok(boardReportService.findAll());
    }

    @GetMapping("/boards")
    public ResponseEntity<List<BoardResponseDto>> getBoards(HttpSession session) {
        if (!isAdmin(session)) {
            return forbiddenOrUnauthorized(session);
        }

        return ResponseEntity.ok(boardService.findAllForAdmin());
    }

    @PatchMapping("/boards/{boardId}/notice")
    public ResponseEntity<Void> updateNotice(
            @PathVariable Long boardId,
            @RequestParam boolean notice,
            HttpSession session
    ) {
        if (!isAdmin(session)) {
            return forbiddenOrUnauthorized(session);
        }

        boardService.updateNotice(boardId, notice);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/replies")
    public ResponseEntity<List<ReplyResponseDto>> getReplies(HttpSession session) {
        if (!isAdmin(session)) {
            return forbiddenOrUnauthorized(session);
        }

        return ResponseEntity.ok(replyService.findAllForAdmin());
    }

    @DeleteMapping("/replies/{replyId}")
    public ResponseEntity<Void> deleteReply(@PathVariable Long replyId, HttpSession session) {
        if (!isAdmin(session)) {
            return forbiddenOrUnauthorized(session);
        }

        replyService.deleteReply(replyId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/reports/{reportId}/resolve")
    public ResponseEntity<Void> resolveReport(@PathVariable Long reportId, HttpSession session) {
        if (!isAdmin(session)) {
            return forbiddenOrUnauthorized(session);
        }

        boardReportService.resolve(reportId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/boards/{boardId}")
    public ResponseEntity<Void> deleteReportedBoard(@PathVariable Long boardId, HttpSession session) {
        if (!isAdmin(session)) {
            return forbiddenOrUnauthorized(session);
        }

        boardService.delete(boardId);
        return ResponseEntity.noContent().build();
    }

    private boolean isAdmin(HttpSession session) {
        Long loginMemberId = (Long) session.getAttribute("loginMemberId");
        return loginMemberId != null && memberService.isAdmin(loginMemberId);
    }

    private <T> ResponseEntity<T> forbiddenOrUnauthorized(HttpSession session) {
        if (session.getAttribute("loginMemberId") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
