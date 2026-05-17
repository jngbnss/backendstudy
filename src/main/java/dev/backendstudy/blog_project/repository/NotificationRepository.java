package dev.backendstudy.blog_project.repository;

import dev.backendstudy.blog_project.entity.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReceiverIdOrderByCreatedAtDesc(Long receiverId);
    long countByReceiverIdAndReadFalse(Long receiverId);
}
