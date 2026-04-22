package dev.backendstudy.blog_project;

import dev.backendstudy.blog_project.dto.BoardRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController // JSON 데이터를 주고받는 REST API용 컨트롤러임을 선언
@RequiredArgsConstructor //final이 붙은 BoardService를 생성자 주입으로 가져옴
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/api/boards")
    public Long createdBoard(@RequestBody BoardRequestDto requestDto){
        //1. 브라우저에서 보낸 JSON 데이터가 requestDto에 담겨 들어옴
        //2. 서비스의 save 로직을 호출하여 DB에 저장
        //3. 저장된 게시글의 ID를 반환
        return boardService.save(requestDto);
    }
}
// 리퀘스트 바디로 제이슨을 풀어서 자바로 넘김