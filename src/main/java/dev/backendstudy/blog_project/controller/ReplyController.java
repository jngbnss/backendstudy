package dev.backendstudy.blog_project.controller;

import dev.backendstudy.blog_project.dto.ReplyRequestDto;
import dev.backendstudy.blog_project.service.ReplyService;
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

    @PostMapping("/boards/{boardId}/replies")
    public ResponseEntity<Long> createReply(@PathVariable Long boardId, @RequestBody ReplyRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(replyService.saveReply(boardId, requestDto));
    }

    @PutMapping("/replies/{replyId}")
    public ResponseEntity<Long> updateReply(@PathVariable Long replyId, @RequestBody ReplyRequestDto requestDto) {
        return ResponseEntity.ok(replyService.updateReply(replyId, requestDto));
    }

    @DeleteMapping("/replies/{replyId}")
    public ResponseEntity<Void> deleteReply(@PathVariable Long replyId) {
        replyService.deleteReply(replyId);
        return ResponseEntity.noContent().build();
    }
}