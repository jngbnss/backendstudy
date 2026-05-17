package dev.backendstudy.blog_project.service;

import dev.backendstudy.blog_project.dto.notification.NotificationResponseDto;
import dev.backendstudy.blog_project.entity.Board;
import dev.backendstudy.blog_project.entity.Member;
import dev.backendstudy.blog_project.entity.Notification;
import dev.backendstudy.blog_project.repository.NotificationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {
    private final NotificationRepository notificationRepository;

    @Transactional
    public void notifyBoardReply(Board board, Member sender) {
        Member receiver = board.getMember();
        if (receiver.getId().equals(sender.getId())) {
            return;
        }
        notificationRepository.save(new Notification(
                receiver,
                sender,
                board,
                Notification.BOARD_REPLY,
                sender.getUsername() + " commented on your post."
        ));
    }

    @Transactional
    public void notifyReplyReply(Member receiver, Member sender, Board board) {
        if (receiver.getId().equals(sender.getId())) {
            return;
        }
        notificationRepository.save(new Notification(
                receiver,
                sender,
                board,
                Notification.REPLY_REPLY,
                sender.getUsername() + " replied to your comment."
        ));
    }

    public List<NotificationResponseDto> findMyNotifications(Long memberId) {
        return notificationRepository.findByReceiverIdOrderByCreatedAtDesc(memberId).stream()
                .map(NotificationResponseDto::new)
                .toList();
    }

    public long countUnread(Long memberId) {
        if (memberId == null) {
            return 0;
        }
        return notificationRepository.countByReceiverIdAndReadFalse(memberId);
    }

    @Transactional
    public void markAsRead(Long notificationId, Long memberId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found. id=" + notificationId));
        if (!notification.getReceiver().getId().equals(memberId)) {
            throw new IllegalArgumentException("Notification owner mismatch.");
        }
        notification.markAsRead();
    }
}
