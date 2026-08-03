package dev.backendstudy.board.domain.post;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 메모리 저장소. DB 없이 Map에 게시글을 담는다.
 * 서버를 재시작하면 데이터는 사라진다(휘발성).
 */
@Repository
public class MemoryPostRepository implements PostRepository {

    private final Map<Long, Post> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    @Override
    public Post save(Post post) {
        if (post.getId() == null) {
            post.assignId(sequence.incrementAndGet());
        }
        store.put(post.getId(), post);
        return post;
    }

    @Override
    public Optional<Post> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Post> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void delete(Post post) {
        store.remove(post.getId());
    }
}
