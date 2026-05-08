package dev.backendstudy.blog_project.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor // dto에서는 jpa용이 아님 @Entity가 붙어야 필요한거고, DTO에서는 보통 Jackson이 JSON요청을 객체로 바꿀때 필요해서
@AllArgsConstructor//이건 그냥 편하게 쓰려고하나봄?
public class MemberSignupRequestDto {
    private String username;
    private String email;
    private String password;
}

/**
 * 리플렉션 기술을 써서 값을 넣음
 * 핵심은 일단 빈 객체를 만들 수 있어야 함
 * 그래서 기본 생성자가 필요
 * 실제로 이것도 리플렉션이나 프록시 기술을 사용함
 *
 *----
 * 정리하면 둘 다 이유는 비슷해.
 *
 *   Jackson: JSON 데이터를 Java DTO 객체로 만들기 위해
 *   JPA: DB row 데이터를 Java Entity 객체로 만들기 위해
 *
 *   둘 다 외부 데이터에서 객체를 “복원”해야 해.
 *
 *   그래서 가장 단순한 방식은:
 *
 *   1. 기본 생성자로 빈 객체를 만든다
 *   2. 필드에 값을 채운다
 *
 *   이거야.
 *
 *   다만 목적은 달라.
 *
 *   DTO + Jackson = 요청/응답 JSON 변환
 *   Entity + JPA = DB 데이터 변환/영속성 관리
 *   ----
 */