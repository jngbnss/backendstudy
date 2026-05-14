package dev.backendstudy.blog_project.service;

import dev.backendstudy.blog_project.dto.member.MemberLoginRequestDto;
import dev.backendstudy.blog_project.dto.member.MemberResponseDto;
import dev.backendstudy.blog_project.dto.member.MemberSignupRequestDto;
import dev.backendstudy.blog_project.dto.member.MemberUpdateRequestDto;
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
        if (memberRepository.existsByLoginId(requestDto.getLoginId())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        Member member = new Member(
                requestDto.getUsername(),
                requestDto.getLoginId(),
                encodedPassword
        );

        return memberRepository.save(member).getId();
    }

    public Member login(MemberLoginRequestDto requestDto) {
        Member member = memberRepository.findByLoginId(requestDto.getLoginId())
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        return member;
    }

    public boolean isLoginIdAvailable(String loginId) {
        return !memberRepository.existsByLoginId(loginId);
    }

    public MemberResponseDto findMyInfo(Long memberId) {
        return new MemberResponseDto(findMember(memberId));
    }

    public Member findMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        return member;
    }

    @Transactional
    public void updateMyInfo(Long memberId, MemberUpdateRequestDto requestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        member.updateUsername(requestDto.getUsername());
    }
}
