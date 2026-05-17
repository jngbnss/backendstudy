package dev.backendstudy.blog_project.controller;

import dev.backendstudy.blog_project.service.BoardService;
import dev.backendstudy.blog_project.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BlogViewController {
    private final BoardService boardService;
    private final MemberService memberService;

    @GetMapping
    public String getBoardList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "latest") String sort,
            Model model,
            HttpSession session
    ) {
        int pageNumber = Math.max(page, 0);
        var pageable = PageRequest.of(pageNumber, 10, createSort(sort));
        var boardPage = boardService.findPage(keyword, pageable);

        model.addAttribute("boardPage", boardPage);
        model.addAttribute("boards", boardPage.getContent());
        model.addAttribute("keyword", keyword == null ? "" : keyword);
        model.addAttribute("sort", sort);
        model.addAttribute("pageTitle", "게시글 목록");
        model.addAttribute("listPath", "/boards");
        model.addAttribute("isLoggedIn", session.getAttribute("loginMemberId") != null);
        return "boards/boardList";
    }

    @GetMapping("/my")
    public String getMyBoardList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "latest") String sort,
            Model model,
            HttpSession session
    ) {
        Long loginMemberId = (Long) session.getAttribute("loginMemberId");
        if (loginMemberId == null) {
            return "redirect:/login";
        }

        int pageNumber = Math.max(page, 0);
        var pageable = PageRequest.of(pageNumber, 10, createSort(sort));
        var boardPage = boardService.findMyPage(loginMemberId, keyword, pageable);

        model.addAttribute("boardPage", boardPage);
        model.addAttribute("boards", boardPage.getContent());
        model.addAttribute("keyword", keyword == null ? "" : keyword);
        model.addAttribute("sort", sort);
        model.addAttribute("pageTitle", "내가 작성한 글");
        model.addAttribute("listPath", "/boards/my");
        model.addAttribute("isLoggedIn", true);
        return "boards/boardList";
    }

    @GetMapping("/{boardId}")
    public String getBoardPage(@PathVariable Long boardId, Model model, HttpSession session) {
        Long loginMemberId = (Long) session.getAttribute("loginMemberId");
        var board = boardService.findByIdForView(boardId, loginMemberId);
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

    private Sort createSort(String sort) {
        Sort selectedSort = switch (sort) {
            case "date" -> Sort.by(Sort.Direction.ASC, "createdAt")
                    .and(Sort.by(Sort.Direction.ASC, "id"));
            case "views" -> Sort.by(Sort.Direction.DESC, "viewCount")
                    .and(Sort.by(Sort.Direction.DESC, "id"));
            case "popular" -> Sort.by(Sort.Direction.DESC, "likeCount")
                    .and(Sort.by(Sort.Direction.ASC, "dislikeCount"))
                    .and(Sort.by(Sort.Direction.DESC, "id"));
            default -> Sort.by(Sort.Direction.DESC, "createdAt")
                    .and(Sort.by(Sort.Direction.DESC, "id"));
        };

        return Sort.by(Sort.Direction.DESC, "notice").and(selectedSort);
    }
}
