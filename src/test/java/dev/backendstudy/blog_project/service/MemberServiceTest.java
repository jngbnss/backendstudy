package dev.backendstudy.blog_project.service;

import static org.assertj.core.api.Assertions.assertThat;

import dev.backendstudy.blog_project.dto.member.MemberUpdateRequestDto;
import dev.backendstudy.blog_project.entity.Board;
import dev.backendstudy.blog_project.entity.Member;
import dev.backendstudy.blog_project.entity.Reply;
import dev.backendstudy.blog_project.repository.BoardRepository;
import dev.backendstudy.blog_project.repository.MemberRepository;
import dev.backendstudy.blog_project.repository.ReplyRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("update username changes board and reply writer names through member relation")
    void updateMyInfo_updatesWriterNames() {
        Member member = memberRepository.save(new Member("oldName", "loginId", "password"));
        Board board = boardRepository.save(new Board("title", "content", member));
        Reply reply = replyRepository.save(new Reply("reply", member, board));

        memberService.updateMyInfo(member.getId(), new MemberUpdateRequestDto("newName"));
        entityManager.flush();
        entityManager.clear();

        Member updatedMember = memberRepository.findById(member.getId()).orElseThrow();
        Board updatedBoard = boardRepository.findById(board.getId()).orElseThrow();
        Reply updatedReply = replyRepository.findById(reply.getId()).orElseThrow();

        assertThat(updatedMember.getUsername()).isEqualTo("newName");
        assertThat(updatedBoard.getWriter()).isEqualTo("newName");
        assertThat(updatedReply.getWriter()).isEqualTo("newName");
    }
}
