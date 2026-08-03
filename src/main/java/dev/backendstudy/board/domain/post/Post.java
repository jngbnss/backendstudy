package dev.backendstudy.board.domain.post;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 순수 자바 객체(POJO). JPA 엔티티가 아니라 메모리 저장소에 담기는 도메인 모델.
 */
@Getter
public class Post {

    private Long id;
    private String title;
    private String content;
    private String author;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Post(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    /** 저장소에서 id를 발급해 넣어줄 때 사용 */
    public void assignId(Long id) {
        this.id = id;
    }

    /** 수정 (JPA 더티 체킹 대신 직접 반영) */
    public void update(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.updatedAt = LocalDateTime.now();
    }
}
