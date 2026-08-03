package dev.backendstudy.board.domain.post;

import java.util.List;
import java.util.Optional;

/**
 * 저장소 인터페이스. 지금은 메모리 구현(MemoryPostRepository)을 쓰지만,
 * 나중에 JPA/JDBC 구현으로 갈아끼울 수 있게 인터페이스로 분리.
 */
public interface PostRepository {

    Post save(Post post);

    Optional<Post> findById(Long id);

    List<Post> findAll();

    void delete(Post post);
}
