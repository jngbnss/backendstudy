package dev.backendstudy.blog_project.dto.notification;

import dev.backendstudy.blog_project.entity.Notification;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class NotificationResponseDto {
    private Long id;
    private String type;
    private String message;
    private Long boardId;
    private String boardTitle;
    private String sender;
    private boolean read;
    private LocalDateTime createdAt;

    public NotificationResponseDto(Notification notification) {
        this.id = notification.getId();
        this.type = notification.getType();
        this.message = notification.getMessage();
        this.boardId = notification.getBoard().getId();
        this.boardTitle = notification.getBoard().getTitle();
        this.sender = notification.getSender().getUsername();
        this.read = notification.isRead();
        this.createdAt = notification.getCreatedAt();
    }
}
