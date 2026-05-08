package dev.backendstudy.blog_project.service;

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
    //회원가입
    //회원조회
    //이메일 중복 확인
    //회원 정보 수정
    // 비밀번호 변경
    // 회원 탈퇴


}
