package dev.backendstudy.blog_project.controller;

import dev.backendstudy.blog_project.dto.member.MemberLoginRequestDto;
import dev.backendstudy.blog_project.dto.member.MemberResponseDto;
import dev.backendstudy.blog_project.dto.member.MemberSignupRequestDto;
import dev.backendstudy.blog_project.dto.member.MemberUpdateRequestDto;
import dev.backendstudy.blog_project.entity.Member;
import dev.backendstudy.blog_project.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/login")
    public ResponseEntity<Long>login(
            @RequestBody MemberLoginRequestDto requestDto,
            HttpSession session
            ){
        Member member = memberService.login(requestDto);

        session.setAttribute("loginMemberId", member.getId());
        session.setAttribute("loginMemberEmail",member.getEmail());

        return ResponseEntity.ok(member.getId());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void>logout(HttpSession session){
        session.invalidate();
        return ResponseEntity.ok().build();

    }
    /*
      처음 공부용으로는 이 정도가 적당합니다. 다음 단계는 “로그인한 사람만 글쓰기 가능”, “작성자를 직접 입력하지 않고 로그인한 회원 이름으로
  저장” 쪽으로 이어가면 됩니다.
     */

    @GetMapping("/me")
    public ResponseEntity<MemberResponseDto>myInfo(HttpSession session){
        Long loginMemberId = (Long) session.getAttribute("loginMemberId");

        if(loginMemberId==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        MemberResponseDto response = memberService.findMyInfo(loginMemberId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    public ResponseEntity<Void>updateMyInfo(
            @RequestBody MemberUpdateRequestDto requestDto,
            HttpSession session
    ){
        Long loginMemberId = (Long) session.getAttribute("loginMemberId");
        if(loginMemberId==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        memberService.updateMyInfo(loginMemberId,requestDto);
        return ResponseEntity.ok().build();
    }
}
