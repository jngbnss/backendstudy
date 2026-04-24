package dev.backendstudy.blog_project.controller;

import dev.backendstudy.blog_project.dto.BoardResponseDto;
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

    // 1. 게시글 목록 페이지
    @GetMapping("/boards")
    public String getBoardList(Model model) {
        model.addAttribute("boards", boardService.findAll());
        return "boardList"; // boardList.html로 이동
    }

    // 2. 게시글 상세 페이지 (기존 코드)
    @GetMapping("/boards/{boardId}")
    public String getBoardPage(@PathVariable Long boardId, Model model) {
        model.addAttribute("board", boardService.findById(boardId));
        return "boardDetail";
    }

    @GetMapping("/boards/update/{boardId}")
    public String getUpdatePage(@PathVariable Long boardId, Model model) {
        model.addAttribute("board", boardService.findById(boardId));
        return "boardUpdate"; // boardUpdate.html로 이동
    }
    // BlogViewController.java

    @GetMapping("/boards/write")
    public String getWritePage() {
        return "boardWrite"; // boardWrite.html로 이동
    }
}