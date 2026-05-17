package dev.backendstudy.blog_project.dto.board;

import dev.backendstudy.blog_project.entity.BoardAttachment;
import lombok.Getter;

@Getter
public class BoardAttachmentResponseDto {
    private Long id;
    private String originalFilename;
    private String contentType;
    private long size;
    private String fileUrl;
    private boolean image;

    public BoardAttachmentResponseDto(BoardAttachment attachment) {
        this.id = attachment.getId();
        this.originalFilename = attachment.getOriginalFilename();
        this.contentType = attachment.getContentType();
        this.size = attachment.getSize();
        this.fileUrl = attachment.getFileUrl();
        this.image = attachment.isImage();
    }
}
