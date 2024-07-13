package org.blog.blogflatformproject.common.controller;

import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.board.domain.Board;
import org.blog.blogflatformproject.board.service.impl.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CommonController {

    private final BoardService boardService;

    @GetMapping("/")
    public String mainPage(Model model){

        List<Board> boardList = boardService.getBoardListOrderByCreatedAt();
        model.addAttribute("boardList" , boardList);
        return "pages/main";
    }
}
