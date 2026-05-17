package dev.backendstudy.blog_project.repository;

import dev.backendstudy.blog_project.entity.BoardReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardReportRepository extends JpaRepository<BoardReport, Long> {
}
