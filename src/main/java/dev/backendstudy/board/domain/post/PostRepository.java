package dev.backendstudy.board.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 최신순 목록
    List<Post> findAllByOrderByCreatedAtDesc();
}
