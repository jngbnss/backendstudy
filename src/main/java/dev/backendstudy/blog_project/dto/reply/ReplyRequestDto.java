package dev.backendstudy.blog_project.dto.reply;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReplyRequestDto {
    private String content;
    private String writer;
}
