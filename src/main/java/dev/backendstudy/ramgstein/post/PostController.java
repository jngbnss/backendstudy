package dev.backendstudy.ramgstein.post;

import dev.backendstudy.ramgstein.post.dto.PostCreateRequest;
import dev.backendstudy.ramgstein.post.dto.PostResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService  postService;

    @PostMapping
    public PostResponse create(@Valid @RequestBody PostCreateRequest request){
        Post post = postService.create(request.title(),request.content(),request.writer());
        return PostResponse.from(post);
    }

    @GetMapping
    public List<PostResponse> findAll(){
        List<Post> posts = postService.findAll();

        List<PostResponse> responses = new ArrayList<>();
        for (Post post : posts) {
            responses.add(PostResponse.from(post));
        }
        return responses;
    }

    @GetMapping("/{postId}")
    public PostResponse findOne(@PathVariable Long postId) {
        return PostResponse.from(postService.findOne(postId));
    }
}
