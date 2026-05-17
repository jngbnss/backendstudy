package dev.backendstudy.blog_project;

import dev.backendstudy.blog_project.entity.Board;
import dev.backendstudy.blog_project.entity.Member;
import dev.backendstudy.blog_project.entity.Reply;
import dev.backendstudy.blog_project.repository.BoardRepository;
import dev.backendstudy.blog_project.repository.MemberRepository;
import dev.backendstudy.blog_project.repository.ReplyRepository;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@ConditionalOnProperty(name = "data-init.enabled", havingValue = "true")
@RequiredArgsConstructor
public class DataInit implements CommandLineRunner {
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (memberRepository.existsByLoginId("admin")) {
            return;
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Member member1 = memberRepository.save(
                new Member("관리자", "admin", encoder.encode("1"))
        );

        Member member2 = memberRepository.save(
                new Member("종빈쓰", "1", encoder.encode("1"))
        );

        member1.updateProfile(member1.getUsername(), "/images/dummy/admin-profile.jpg");
        member2.updateProfile(member2.getUsername(), "/images/dummy/user-profile.jpg");

        Board board1 = null;
        for (int i = 1; i <= 100; i++) {
            Member writer = i <= 3 ? member1 : member2;
            String titlePrefix = writer.isAdmin() ? "공지사항 " : "더미 게시글 제목 ";

            Board board = boardRepository.save(
                    new Board(titlePrefix + i, "더미 게시글 내용 " + i, writer)
            );

            if (writer.isAdmin()) {
                board.markAsNotice();
            }

            boardRepository.updateDummyFields(
                    board.getId(),
                    createdAtBySequence(i),
                    randomLong(0, 1000),
                    randomLong(0, 200),
                    randomLong(0, 50)
            );

            if (i == 1) {
                board1 = board;
            }
        }

        replyRepository.save(
                new Reply("비밀 댓글입니다.", member2, board1)
        );

        replyRepository.save(
                new Reply("기밀 댓글입니다.", member1, board1)
        );
    }

    private LocalDateTime createdAtBySequence(int sequence) {
        return LocalDateTime.now()
                .minusDays(100L - sequence)
                .withHour(9)
                .withMinute(sequence % 60)
                .withSecond(0)
                .withNano(0);
    }

    private long randomLong(long minInclusive, long maxInclusive) {
        return ThreadLocalRandom.current().nextLong(minInclusive, maxInclusive + 1);
    }
}
