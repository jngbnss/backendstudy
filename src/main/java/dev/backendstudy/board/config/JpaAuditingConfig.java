package dev.backendstudy.board.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * created_at, updated_at 자동 기록(JPA Auditing) 활성화.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
