package org.blog.blogflatformproject.board.controller;

import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.board.domain.Board;
import org.blog.blogflatformproject.board.repository.service.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardRestController {


    private final BoardService boardService;

    @PutMapping("/updateboard")
    public ResponseEntity<String> updateBoard(@ModelAttribute Board board){
        if(boardService.updateBoard(board)!=null){
            return ResponseEntity.ok("ok");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("게시물 수정 실패");
        }
    }
}
