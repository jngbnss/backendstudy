package dev.backendstudy.blog_project;

import dev.backendstudy.blog_project.entity.Board;
import dev.backendstudy.blog_project.entity.Member;
import dev.backendstudy.blog_project.entity.Reply;
import dev.backendstudy.blog_project.repository.BoardRepository;
import dev.backendstudy.blog_project.repository.MemberRepository;
import dev.backendstudy.blog_project.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "data-init.enabled", havingValue = "true")
@RequiredArgsConstructor
public class DataInit implements CommandLineRunner {
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;

    @Override
    public void run(String... args) {
        if (memberRepository.existsByLoginId("admin")) {
            return;
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Member member1 = memberRepository.save(
                new Member("관리자", "admin", encoder.encode("answhdqlscjswo"))
        );

        Member member2 = memberRepository.save(
                new Member("최강싸피", "jngbnss", encoder.encode("answhdqlscjswo"))
        );

        Board board1 = boardRepository.save(
                new Board("공지사항", "공지사항 입니다.", member1)
        );

        boardRepository.save(
                new Board("버그발견하면 알려주세요! ", "땡큐!", member2)
        );

        replyRepository.save(
                new Reply("비밀 댓글입니다.", member2, board1)
        );

        replyRepository.save(
                new Reply("기밀 댓글입니다.", member1, board1)
        );
    }
}
