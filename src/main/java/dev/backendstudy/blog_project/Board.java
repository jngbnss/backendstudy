package dev.backendstudy.blog_project;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor // 파라미터가 없는 기본 자동 생성자 자동생성
@EntityListeners(AuditingEntityListener.class) // 생성/ 수정 시간 자동 기록을 위해 필요
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB의 auto increment 사용
    private Long id;

    @Column(nullable = false, length = 100) // 제목은 필수 , 길이는 100자 제한
    private String title;
    @Column(columnDefinition = "TEXT", nullable = false)// 내용은 길 수 있으므로
    private String content;
    @Column(nullable = false)
    private String writer;
    @CreatedDate
    @Column(updatable = false) // 생성 시간은 수정되지 않도록 설정
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    //게시글 생성을 위한 생성자

    public Board(String title, String content, String writer) {
        this.title = title;
        this.content = content;
        this.writer = writer;
    }

    //게시글 수정을 위한 메서드
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
        //setter 대신 비즈니스 메서드 사용권장
    }
}
