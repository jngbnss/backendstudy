package dev.backendstudy.blog_project.controller;

import dev.backendstudy.blog_project.dto.member.MemberResponseDto;
import dev.backendstudy.blog_project.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MemberViewController {
    private final MemberService memberService;

    @GetMapping("/signup")
    public String signupPage(){
        return "members/signup";
    }

    // 나중에 rest컨트롤러로 가면 필요없음
    @GetMapping("/login")
    public String loginPage(){
        return "members/login";
    }
    @GetMapping("/members/me")
    public String myProfilePage(HttpSession session, Model model){
        Long loginMemberId = (Long) session.getAttribute("loginMemberId");

        if (loginMemberId != null) {
            MemberResponseDto member = memberService.findMyInfo(loginMemberId);
            model.addAttribute("member", member);
        }

        return "members/myProfile";
    }
}
