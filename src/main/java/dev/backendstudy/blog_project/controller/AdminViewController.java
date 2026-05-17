package dev.backendstudy.blog_project.controller;

import dev.backendstudy.blog_project.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminViewController {
    private final MemberService memberService;

    @GetMapping("/admin")
    public String adminPage(HttpSession session, Model model) {
        Long loginMemberId = (Long) session.getAttribute("loginMemberId");
        if (loginMemberId == null) {
            return "redirect:/login";
        }
        if (!memberService.isAdmin(loginMemberId)) {
            return "redirect:/boards";
        }

        model.addAttribute("isLoggedIn", true);
        model.addAttribute("isAdmin", true);
        return "admin/dashboard";
    }
}
