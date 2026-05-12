package dev.backendstudy.blog_project.dto.member;

import dev.backendstudy.blog_project.entity.Member;
import lombok.Getter;

@Getter
public class MemberResponseDto {
    private Long id;
    private String username;
    private String email;

    public MemberResponseDto(Member member){
        this.id = member.getId();
        this.username = member.getUsername();
        this.email=member.getEmail();
    }
}
