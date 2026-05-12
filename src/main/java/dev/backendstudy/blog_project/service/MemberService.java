package dev.backendstudy.blog_project.service;

import dev.backendstudy.blog_project.dto.member.MemberLoginRequestDto;
import dev.backendstudy.blog_project.dto.member.MemberSignupRequestDto;
import dev.backendstudy.blog_project.entity.Member;
import dev.backendstudy.blog_project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    //회원가입
    @Transactional
    public Long signup(MemberSignupRequestDto requestDto) {
        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        Member member = new Member(
                requestDto.getUsername(),
                requestDto.getEmail(),
                encodedPassword

        );

        return memberRepository.save(member).getId();
    }

    //로그인
    public Member login(MemberLoginRequestDto requestDto){
        Member member = memberRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다."));

        if(!passwordEncoder.matches(requestDto.getPassword(),member.getPassword())){
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }
        return member;

    }


    //회원가입
    //로그인
    //회원조회
    //이메일 중복 확인
    //회원 정보 수정
    // 비밀번호 변경
    // 회원 탈퇴


}
