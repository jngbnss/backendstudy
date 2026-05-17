package dev.backendstudy.blog_project.controller;

import dev.backendstudy.blog_project.dto.board.BoardRequestDto;
import dev.backendstudy.blog_project.dto.board.BoardResponseDto;
import dev.backendstudy.blog_project.dto.board.BoardUpdateDto;
import dev.backendstudy.blog_project.entity.BoardReaction;
import dev.backendstudy.blog_project.entity.Member;
import dev.backendstudy.blog_project.service.BoardService;
import dev.backendstudy.blog_project.service.MemberService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {
    private final BoardService boardService;
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Long> createdBoard(@RequestBody BoardRequestDto requestDto, HttpSession session) {
        Long loginMemberId = (Long) session.getAttribute("loginMemberId");

        if (loginMemberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Member member = memberService.findMember(loginMemberId);
        Long id = boardService.save(requestDto, member);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @GetMapping
    public List<BoardResponseDto> getAllBoard() {
        return boardService.findAll();
    }

    @GetMapping("/{id}")
    public BoardResponseDto getBoard(@PathVariable Long id) {
        return boardService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBoard(
            @PathVariable Long id,
            @RequestBody BoardUpdateDto requestDto,
            HttpSession session
    ) {
        Long loginMemberId = (Long) session.getAttribute("loginMemberId");

        if (loginMemberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!boardService.isWriter(id, loginMemberId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        boardService.update(id, requestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id, HttpSession session) {
        Long loginMemberId = (Long) session.getAttribute("loginMemberId");

        if (loginMemberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!boardService.isWriter(id, loginMemberId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        boardService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likeBoard(@PathVariable Long id, HttpSession session) {
        return react(id, session, BoardReaction.LIKE);
    }

    @PostMapping("/{id}/dislike")
    public ResponseEntity<Void> dislikeBoard(@PathVariable Long id, HttpSession session) {
        return react(id, session, BoardReaction.DISLIKE);
    }

    private ResponseEntity<Void> react(Long id, HttpSession session, String reactionType) {
        Long loginMemberId = (Long) session.getAttribute("loginMemberId");

        if (loginMemberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Member member = memberService.findMember(loginMemberId);
        boardService.react(id, member, reactionType);
        return ResponseEntity.ok().build();
    }
}
