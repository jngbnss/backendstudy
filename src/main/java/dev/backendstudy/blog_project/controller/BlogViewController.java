package dev.backendstudy.blog_project.controller;

import dev.backendstudy.blog_project.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class BlogViewController {
    private final BoardService boardService;

    @GetMapping("/boards")
    public String getBoardList(Model model) {
        model.addAttribute("boards", boardService.findAll());
        return "boardList";
    }

    @GetMapping("/boards/{boardId}")
    public String getBoardPage(@PathVariable Long boardId, Model model) {
        model.addAttribute("board", boardService.findById(boardId));
        return "boardDetail";
    }

    @GetMapping("/boards/update/{boardId}")
    public String getUpdatePage(@PathVariable Long boardId, Model model) {
        model.addAttribute("board", boardService.findById(boardId));
        return "boardUpdate";
    }

    @GetMapping("/boards/write")
    public String getWritePage() {
        return "boardWrite";
    }
}