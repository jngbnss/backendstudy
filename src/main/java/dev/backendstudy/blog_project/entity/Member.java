package dev.backendstudy.blog_project.entity;

import jakarta.persistence.*;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor // 지금 왜 필요하지?
/**
 * @NoArgsConstructor는 JPA 때문에 붙인 것
 * JPA가 DB에서 Member 데이터를 읽어서 객체로 만들 때 내부적으로 기본 생성자,
 * 즉 파라미터 없는 생성자가 필요함
 */
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,length = 50)
    private String username;

    @Column(nullable = false,unique = true,length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    public Member(String username,String email,String password){
        // pk인 아이디를 제외한 생성자
        this.username = username;
        this.email = email;
        this.password= password;
    }

    public void updateUsername(String username){
        this.username= username;
    }

}
