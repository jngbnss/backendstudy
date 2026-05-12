package dev.backendstudy.blog_project.controller;

import dev.backendstudy.blog_project.service.BoardService;
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
    public String getBoardList(Model model) {
        model.addAttribute("boards", boardService.findAll());
        return "boardList";
    }

    @GetMapping("/{boardId}")
    public String getBoardPage(@PathVariable Long boardId, Model model) {
        model.addAttribute("board", boardService.findById(boardId));
        return "boardDetail";
    }

    @GetMapping("/update/{boardId}")
    public String getUpdatePage(@PathVariable Long boardId, Model model) {
        model.addAttribute("board", boardService.findById(boardId));
        return "boardUpdate";
    }

    @GetMapping("/write")
    public String getWritePage() {
        return "boardWrite";
    }
}
