package dev.backendstudy.blog_project.repository;

import dev.backendstudy.blog_project.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByLoginId(String loginId);
    boolean existsByLoginIdAndDeletedFalse(String loginId);
    Optional<Member> findByLoginId(String loginId);
    Optional<Member> findByLoginIdAndDeletedFalse(String loginId);
    List<Member> findByDeletedFalse();
}
