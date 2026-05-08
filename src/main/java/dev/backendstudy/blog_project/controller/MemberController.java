package dev.backendstudy.blog_project.controller;

import dev.backendstudy.blog_project.dto.member.MemberSignupRequestDto;
import dev.backendstudy.blog_project.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;
    @PostMapping("/signup")
    public ResponseEntity<Long>signup(@RequestBody MemberSignupRequestDto requestDto){
        Long memberId = memberService.signup(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberId);
    }
}
