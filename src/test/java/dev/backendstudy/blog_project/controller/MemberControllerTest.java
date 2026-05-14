package dev.backendstudy.blog_project.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.backendstudy.blog_project.entity.Member;
import dev.backendstudy.blog_project.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("check login id available")
    void checkLoginIdAvailable() throws Exception {
        mockMvc.perform(get("/api/members/check-login-id")
                        .param("loginId", "newId"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("check login id duplicated")
    void checkLoginIdDuplicated() throws Exception {
        memberRepository.save(new Member("user", "usedId", "password"));

        mockMvc.perform(get("/api/members/check-login-id")
                        .param("loginId", "usedId"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }
}
