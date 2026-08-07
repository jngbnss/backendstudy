package dev.backendstudy.ramgstein.post;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length =100)
    private String title;

    @Column(nullable = false,columnDefinition = "TEXT")
    private String  content;

    @Column(nullable = false,length = 20)
    private String writer;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private  LocalDateTime updatedAt;

    public Post(String title,String content,String writer){
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public void update(String title,String content){
        this.title = title;
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }
}
