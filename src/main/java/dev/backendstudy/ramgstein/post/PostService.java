package dev.backendstudy.ramgstein.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final PostRepository repository;


    public Post create(String title, String content,String writer){
        Post post = new Post(title,content,writer);
        return repository.save(post);
    }

    @Transactional(readOnly = true)
    public List<Post>findAll(){
        return repository.findAll();
    }
    @Transactional(readOnly = true)
    public Post  findOne(Long  id){
        return repository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("게시글을  찾을 수 없습니다." +
                        "id = "+id));
    }
}
