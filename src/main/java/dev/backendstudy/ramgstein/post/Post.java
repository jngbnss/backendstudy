package dev.backendstudy.ramgstein.post;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //이건 뭘까
//jpa는 파라미터  없는 생성자를 반드시 요구
//public으로 열면  아무 데서나 가능,
//protected로 막으면 같은  패키지  or 상속받은 클래스만 호출
//다른  패키지의 service/controller에서 실수로 못 씀
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length =100) // 20으로 고치면 안되나?
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
    }//이거는  왜 필요한거지?

    public void update(String title,String content){
        this.title = title;
        this.content = content;
    }
    @PrePersist
    void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }
    @PreUpdate
    void onUpdated(){
        this.updatedAt = LocalDateTime.now();
    }
}
