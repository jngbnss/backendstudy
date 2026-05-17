package dev.backendstudy.blog_project.service;

import dev.backendstudy.blog_project.dto.board.BoardAttachmentResponseDto;
import dev.backendstudy.blog_project.entity.Board;
import dev.backendstudy.blog_project.entity.BoardAttachment;
import dev.backendstudy.blog_project.repository.BoardAttachmentRepository;
import dev.backendstudy.blog_project.repository.BoardRepository;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardAttachmentService {
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            "jpg", "jpeg", "png", "gif", "webp", "pdf", "txt", "zip", "doc", "docx", "xls", "xlsx"
    );

    private final BoardAttachmentRepository boardAttachmentRepository;
    private final BoardRepository boardRepository;
    private final Path uploadDir = Paths.get("uploads", "board-attachments").toAbsolutePath().normalize();

    @Transactional
    public List<BoardAttachmentResponseDto> upload(Long boardId, List<MultipartFile> files) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found. id=" + boardId));

        return files.stream()
                .filter(file -> file != null && !file.isEmpty())
                .map(file -> store(board, file))
                .map(BoardAttachmentResponseDto::new)
                .toList();
    }

    public DownloadFile getDownloadFile(Long attachmentId) {
        BoardAttachment attachment = boardAttachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new IllegalArgumentException("Attachment not found. id=" + attachmentId));
        try {
            Path path = uploadDir.resolve(attachment.getStoredFilename()).normalize();
            if (!path.startsWith(uploadDir) || !Files.exists(path)) {
                throw new IllegalArgumentException("Attachment file not found.");
            }
            Resource resource = new UrlResource(path.toUri());
            return new DownloadFile(resource, attachment.getOriginalFilename(), attachment.getContentType());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read attachment.", e);
        }
    }

    @Transactional
    public void delete(Long attachmentId) {
        BoardAttachment attachment = boardAttachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new IllegalArgumentException("Attachment not found. id=" + attachmentId));
        Path path = uploadDir.resolve(attachment.getStoredFilename()).normalize();
        boardAttachmentRepository.delete(attachment);
        try {
            if (path.startsWith(uploadDir)) {
                Files.deleteIfExists(path);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to delete attachment file.", e);
        }
    }

    public boolean canManage(Long attachmentId, Long memberId) {
        BoardAttachment attachment = boardAttachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new IllegalArgumentException("Attachment not found. id=" + attachmentId));
        return attachment.getBoard().getWriterId().equals(memberId);
    }

    private BoardAttachment store(Board board, MultipartFile file) {
        validate(file);
        String originalFilename = file.getOriginalFilename();
        String extension = extractExtension(originalFilename);
        String storedFilename = UUID.randomUUID() + "." + extension;
        Path targetPath = uploadDir.resolve(storedFilename).normalize();

        if (!targetPath.startsWith(uploadDir)) {
            throw new IllegalArgumentException("Invalid file path.");
        }

        try {
            Files.createDirectories(uploadDir);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to store attachment.", e);
        }

        String contentType = file.getContentType() == null ? "application/octet-stream" : file.getContentType();
        BoardAttachment attachment = new BoardAttachment(
                board,
                originalFilename,
                storedFilename,
                contentType,
                file.getSize(),
                "/uploads/board-attachments/" + storedFilename
        );
        return boardAttachmentRepository.save(attachment);
    }

    private void validate(MultipartFile file) {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size must be 10MB or less.");
        }
        String extension = extractExtension(file.getOriginalFilename());
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("Unsupported file extension.");
        }
    }

    private String extractExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            throw new IllegalArgumentException("File extension is required.");
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

    @Getter
    @RequiredArgsConstructor
    public static class DownloadFile {
        private final Resource resource;
        private final String originalFilename;
        private final String contentType;
    }
}
