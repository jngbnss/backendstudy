package dev.backendstudy.blog_project.controller;

import dev.backendstudy.blog_project.dto.board.BoardAttachmentResponseDto;
import dev.backendstudy.blog_project.service.BoardAttachmentService;
import dev.backendstudy.blog_project.service.BoardService;
import dev.backendstudy.blog_project.service.MemberService;
import jakarta.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardAttachmentController {
    private final BoardAttachmentService boardAttachmentService;
    private final BoardService boardService;
    private final MemberService memberService;

    @PostMapping("/boards/{boardId}/attachments")
    public ResponseEntity<List<BoardAttachmentResponseDto>> upload(
            @PathVariable Long boardId,
            @RequestPart("files") List<MultipartFile> files,
            HttpSession session
    ) {
        Long loginMemberId = (Long) session.getAttribute("loginMemberId");
        if (loginMemberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!boardService.canManage(boardId, loginMemberId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(boardAttachmentService.upload(boardId, files));
    }

    @GetMapping("/attachments/{attachmentId}/download")
    public ResponseEntity<Resource> download(@PathVariable Long attachmentId) {
        var file = boardAttachmentService.getDownloadFile(attachmentId);
        ContentDisposition disposition = ContentDisposition.attachment()
                .filename(file.getOriginalFilename(), StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .body(file.getResource());
    }

    @DeleteMapping("/attachments/{attachmentId}")
    public ResponseEntity<Void> delete(@PathVariable Long attachmentId, HttpSession session) {
        Long loginMemberId = (Long) session.getAttribute("loginMemberId");
        if (loginMemberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean canManage = boardAttachmentService.canManage(attachmentId, loginMemberId)
                || memberService.isAdmin(loginMemberId);
        if (!canManage) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        boardAttachmentService.delete(attachmentId);
        return ResponseEntity.noContent().build();
    }
}
