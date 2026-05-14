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
        if (memberRepository.existsByLoginId("hong")) {
            return;
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Member member1 = memberRepository.save(
                new Member("관리자", "admin", encoder.encode("answhdqlscjswo"))
        );

        Member member2 = memberRepository.save(
                new Member("문종빈", "jngbnss", encoder.encode("answhdqlscjswo"))
        );

        Board board1 = boardRepository.save(
                new Board("공지사항", "공지사항 입니다.", member1.getUsername())
        );

        boardRepository.save(
                new Board("난 천재다", "이게 맞다", member2.getUsername())
        );

        replyRepository.save(
                new Reply("비밀 댓글입니다.", member2.getUsername(), board1)
        );

        replyRepository.save(
                new Reply("기밀 댓글입니다.", member1.getUsername(), board1)
        );
    }
}
