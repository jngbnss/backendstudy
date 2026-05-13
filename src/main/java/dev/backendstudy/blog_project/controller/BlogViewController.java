package dev.backendstudy.blog_project.controller;

import dev.backendstudy.blog_project.service.BoardService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BlogViewController {
    private final BoardService boardService;

    @GetMapping
    public String getBoardList(Model model, HttpSession session) {
        model.addAttribute("boards", boardService.findAll());
        model.addAttribute("isLoggedIn", session.getAttribute("loginMemberId") != null);
        return "boards/boardList";
    }

    @GetMapping("/{boardId}")
    public String getBoardPage(@PathVariable Long boardId, Model model, HttpSession session) {
        model.addAttribute("board", boardService.findById(boardId));
        model.addAttribute("isLoggedIn", session.getAttribute("loginMemberId") != null);
        return "boards/boardDetail";
    }

    @GetMapping("/update/{boardId}")
    public String getUpdatePage(@PathVariable Long boardId, Model model) {
        model.addAttribute("board", boardService.findById(boardId));
        return "boards/boardUpdate";
    }

    @GetMapping("/write")
    public String getWritePage(HttpSession session, Model model) {
        model.addAttribute("isLoggedIn", session.getAttribute("loginMemberId") != null);
        return "boards/boardWrite";
    }
}
