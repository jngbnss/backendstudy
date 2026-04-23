package dev.backendstudy.blog_project;

import dev.backendstudy.blog_project.dto.BoardRequestDto;
import dev.backendstudy.blog_project.dto.BoardResponseDto;
import dev.backendstudy.blog_project.dto.BoardUpdateDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // JSON 데이터를 주고받는 REST API용 컨트롤러임을 선언
@RequiredArgsConstructor //final이 붙은 BoardService를 생성자 주입으로 가져옴
@RequestMapping("/api/boards")
public class BoardController {
    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<Long> createdBoard(@RequestBody BoardRequestDto requestDto){
        //1. 브라우저에서 보낸 JSON 데이터가 requestDto에 담겨 들어옴
        //2. 서비스의 save 로직을 호출하여 DB에 저장
        //3. 저장된 게시글의 ID를 반환
        Long id = boardService.save(requestDto);
//        return boardService.save(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }
    @GetMapping
    public List<BoardResponseDto> getAllBoard(){
        return boardService.findAll();
    }

    @GetMapping("/{id}")
    public BoardResponseDto getBoard(@PathVariable Long id){
        return boardService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBoard(@PathVariable Long id, @RequestBody BoardUpdateDto requestDto){
        boardService.update(id,requestDto);
        //수정 완료 후 성공 응답 (200 OK)반환
        return ResponseEntity.ok().build();
    }
    //작성 리스트, 조회, 수정 포스트맨으로 확인완료
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteBoard(@PathVariable Long id){
        boardService.delete(id);
        return ResponseEntity.ok().build();
    }
}
