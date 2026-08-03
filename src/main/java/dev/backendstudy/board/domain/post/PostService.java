package dev.backendstudy.board.domain.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    public List<PostResponse> getPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(PostResponse::from)
                .toList();
    }

    public PostResponse getPost(Long postId) {
        return PostResponse.from(findPost(postId));
    }

    @Transactional
    public Long createPost(PostCreateRequest request) {
        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(request.getAuthor())
                .build();
        return postRepository.save(post).getId();
    }

    @Transactional
    public void updatePost(Long postId, PostUpdateRequest request) {
        Post post = findPost(postId);
        post.update(request.getTitle(), request.getContent(), request.getAuthor());
        // 더티 체킹: 트랜잭션 종료 시 자동 UPDATE
    }

    @Transactional
    public void deletePost(Long postId) {
        Post post = findPost(postId);
        postRepository.delete(post);
    }

    private Post findPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
    }
}
