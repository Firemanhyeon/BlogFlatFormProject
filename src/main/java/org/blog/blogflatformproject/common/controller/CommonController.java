package org.blog.blogflatformproject.common.controller;

import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.board.domain.Board;
import org.blog.blogflatformproject.board.service.impl.BoardServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CommonController {

    private final BoardServiceImpl boardService;

    @GetMapping("/")
    public String mainPage(Model model)throws IllegalArgumentException{
        try{
            List<Board> boardList = boardService.getBoardListOrderByCreatedAt();
            model.addAttribute("boardList" , boardList);
        }catch (Exception e){

        }
        return "pages/main";
    }
}
