package dev.backendstudy.blog_project.controller;

import dev.backendstudy.blog_project.dto.notification.NotificationResponseDto;
import dev.backendstudy.blog_project.service.NotificationService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
class NotificationViewController {
    private final NotificationService notificationService;

    @GetMapping("/notifications")
    public String notificationsPage(HttpSession session, Model model) {
        Long loginMemberId = (Long) session.getAttribute("loginMemberId");
        if (loginMemberId == null) {
            return "redirect:/login";
        }

        model.addAttribute("notifications", notificationService.findMyNotifications(loginMemberId));
        model.addAttribute("unreadNotificationCount", notificationService.countUnread(loginMemberId));
        model.addAttribute("isLoggedIn", true);
        return "notifications/list";
    }
}

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationResponseDto>> getNotifications(HttpSession session) {
        Long loginMemberId = (Long) session.getAttribute("loginMemberId");
        if (loginMemberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(notificationService.findMyNotifications(loginMemberId));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount(HttpSession session) {
        Long loginMemberId = (Long) session.getAttribute("loginMemberId");
        if (loginMemberId == null) {
            return ResponseEntity.ok(0L);
        }
        return ResponseEntity.ok(notificationService.countUnread(loginMemberId));
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long notificationId, HttpSession session) {
        Long loginMemberId = (Long) session.getAttribute("loginMemberId");
        if (loginMemberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        notificationService.markAsRead(notificationId, loginMemberId);
        return ResponseEntity.ok().build();
    }
}
