package dev.backendstudy.board.domain.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public List<PostResponse> getPosts() {
        // id 역순 = 최신 글 먼저
        return postRepository.findAll().stream()
                .sorted(Comparator.comparing(Post::getId).reversed())
                .map(PostResponse::from)
                .toList();
    }

    public PostResponse getPost(Long postId) {
        return PostResponse.from(findPost(postId));
    }

    public Long createPost(PostCreateRequest request) {
        Post post = new Post(request.getTitle(), request.getContent(), request.getAuthor());
        return postRepository.save(post).getId();
    }

    public void updatePost(Long postId, PostUpdateRequest request) {
        Post post = findPost(postId);
        post.update(request.getTitle(), request.getContent(), request.getAuthor());
        postRepository.save(post);
    }

    public void deletePost(Long postId) {
        postRepository.delete(findPost(postId));
    }

    private Post findPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
    }
}
