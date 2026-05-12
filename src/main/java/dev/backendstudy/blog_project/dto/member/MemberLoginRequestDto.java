package dev.backendstudy.blog_project.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
// 왜 no랑 all을 쓸까?
public class MemberLoginRequestDto {
    private String email;
    private String password;
}
