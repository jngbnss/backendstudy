package dev.backendstudy.blog_project.dto.member;

import dev.backendstudy.blog_project.entity.Member;
import dev.backendstudy.blog_project.entity.MemberRole;
import lombok.Getter;

@Getter
public class MemberResponseDto {
    private Long id;
    private String username;
    private String loginId;
    private String profileImageUrl;
    private MemberRole role;
    private boolean deleted;

    public MemberResponseDto(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.loginId = member.getLoginId();
        this.profileImageUrl = member.getProfileImageUrl();
        this.role = member.getRole();
        this.deleted = member.isDeleted();
    }
}
