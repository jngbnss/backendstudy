package dev.backendstudy.blog_project.controller;

import dev.backendstudy.blog_project.dto.CommentRequestDto;
import dev.backendstudy.blog_project.service.CommentService;
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
public class CommentController {
    private final CommentService commentService;

    // 생성
    @PostMapping("/boards/{boardId}/comments")
    public ResponseEntity<Long> createComment(@PathVariable Long boardId, @RequestBody CommentRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.saveComment(boardId, requestDto));
    }

    // 수정
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<Long> updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto requestDto) {
        return ResponseEntity.ok(commentService.updateComment(commentId, requestDto));
    }

    // 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }


}
