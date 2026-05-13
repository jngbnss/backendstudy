package dev.backendstudy.blog_project.controller;

import dev.backendstudy.blog_project.dto.board.BoardRequestDto;
import dev.backendstudy.blog_project.dto.board.BoardResponseDto;
import dev.backendstudy.blog_project.dto.board.BoardUpdateDto;
import dev.backendstudy.blog_project.dto.member.MemberResponseDto;
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

        MemberResponseDto member = memberService.findMyInfo(loginMemberId);
        BoardRequestDto saveRequestDto = new BoardRequestDto(
                requestDto.getTitle(),
                requestDto.getContent(),
                member.getUsername()
        );

        Long id = boardService.save(saveRequestDto);
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

        MemberResponseDto member = memberService.findMyInfo(loginMemberId);
        if (!boardService.isWriter(id, member.getUsername())) {
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

        MemberResponseDto member = memberService.findMyInfo(loginMemberId);
        if (!boardService.isWriter(id, member.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        boardService.delete(id);
        return ResponseEntity.ok().build();
    }
}
