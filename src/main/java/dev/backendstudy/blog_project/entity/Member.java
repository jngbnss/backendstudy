package dev.backendstudy.blog_project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 50)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(length = 500)
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MemberRole role = MemberRole.USER;

    private boolean deleted;

    public Member(String username, String loginId, String password) {
        this.username = username;
        this.loginId = loginId;
        this.password = password;
    }

    public Member(String username, String loginId, String password, MemberRole role) {
        this(username, loginId, password);
        this.role = role;
    }

    public void updateUsername(String username) {
        this.username = username;
    }

    public void updateProfile(String username, String profileImageUrl) {
        this.username = username;
        this.profileImageUrl = profileImageUrl;
    }

    public boolean isAdmin() {
        return this.role == MemberRole.ADMIN;
    }

    public void updateRole(MemberRole role) {
        this.role = role;
    }

    public void withdraw() {
        this.username = "탈퇴한 회원입니다";
        this.loginId = "deleted_" + id;
        this.password = "";
        this.profileImageUrl = null;
        this.deleted = true;
    }
}
