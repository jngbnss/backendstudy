package dev.backendstudy.blog_project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    private boolean deleted;

    public Member(String username, String loginId, String password) {
        this.username = username;
        this.loginId = loginId;
        this.password = password;
    }

    public void updateUsername(String username) {
        this.username = username;
    }

    public void updateProfile(String username, String profileImageUrl) {
        this.username = username;
        this.profileImageUrl = profileImageUrl;
    }

    public boolean isAdmin() {
        return "admin".equals(this.loginId);
    }

    public void withdraw() {
        this.username = "탈퇴한 회원입니다";
        this.loginId = "deleted_" + id;
        this.password = "";
        this.profileImageUrl = null;
        this.deleted = true;
    }
}
