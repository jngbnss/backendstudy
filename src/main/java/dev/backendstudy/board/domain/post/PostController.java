package dev.backendstudy.board.domain.post;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 목록 조회
    @GetMapping
    public List<PostResponse> getPosts() {
        return postService.getPosts();
    }

    // 상세 조회
    @GetMapping("/{postId}")
    public PostResponse getPost(@PathVariable Long postId) {
        return postService.getPost(postId);
    }

    // 작성
    @PostMapping
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostCreateRequest request) {
        Long postId = postService.createPost(request);
        return ResponseEntity
                .created(URI.create("/api/posts/" + postId))
                .body(postService.getPost(postId));
    }

    // 수정
    @PutMapping("/{postId}")
    public PostResponse updatePost(@PathVariable Long postId,
                                   @Valid @RequestBody PostUpdateRequest request) {
        postService.updatePost(postId, request);
        return postService.getPost(postId);
    }

    // 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}
