package dev.backendstudy.blog_project.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor // JSON 데이터를 객체로 바꿀 때 기본 생성자가 꼭 필요
public class BoardRequestDto {
    private String title;
    private String content;
    private String writer;
}
