package dev.backendstudy.blog_project.controller;

import dev.backendstudy.blog_project.service.BoardService;
import dev.backendstudy.blog_project.service.MemberService;
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
    private final MemberService memberService;

    @GetMapping
    public String getBoardList(Model model, HttpSession session) {
        model.addAttribute("boards", boardService.findAll());
        model.addAttribute("isLoggedIn", session.getAttribute("loginMemberId") != null);
        return "boards/boardList";
    }

    @GetMapping("/{boardId}")
    public String getBoardPage(@PathVariable Long boardId, Model model, HttpSession session) {
        var board = boardService.findById(boardId);
        Long loginMemberId = (Long) session.getAttribute("loginMemberId");
        boolean isLoggedIn = loginMemberId != null;
        boolean isWriter = false;

        if (isLoggedIn) {
            isWriter = board.getWriterId().equals(loginMemberId);
            model.addAttribute("loginMemberId", loginMemberId);
        }

        model.addAttribute("board", board);
        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("isWriter", isWriter);
        return "boards/boardDetail";
    }

    @GetMapping("/update/{boardId}")
    public String getUpdatePage(@PathVariable Long boardId, Model model, HttpSession session) {
        Long loginMemberId = (Long) session.getAttribute("loginMemberId");

        if (loginMemberId == null) {
            return "redirect:/login";
        }

        var board = boardService.findById(boardId);
        if (!board.getWriterId().equals(loginMemberId)) {
            return "redirect:/boards/" + boardId;
        }

        model.addAttribute("board", board);
        return "boards/boardUpdate";
    }

    @GetMapping("/write")
    public String getWritePage(HttpSession session, Model model) {
        model.addAttribute("isLoggedIn", session.getAttribute("loginMemberId") != null);
        return "boards/boardWrite";
    }
}
