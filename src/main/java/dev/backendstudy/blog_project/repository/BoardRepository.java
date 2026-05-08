package dev.backendstudy.blog_project.repository;

import dev.backendstudy.blog_project.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}