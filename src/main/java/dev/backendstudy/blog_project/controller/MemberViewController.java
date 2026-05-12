package dev.backendstudy.blog_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberViewController {
    @GetMapping("/signup")
    public String signupPage(){
        return "signup";
    }

    // 나중에 rest컨트롤러로 가면 필요없음
    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }
}
