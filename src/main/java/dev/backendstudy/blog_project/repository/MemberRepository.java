package dev.backendstudy.blog_project.repository;

import dev.backendstudy.blog_project.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
}
