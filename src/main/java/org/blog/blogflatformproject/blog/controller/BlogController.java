package org.blog.blogflatformproject.blog.controller;

import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.blog.domain.Blog;
import org.blog.blogflatformproject.blog.service.BlogService;
import org.blog.blogflatformproject.board.domain.Board;
import org.blog.blogflatformproject.board.service.BoardService;
import org.blog.blogflatformproject.user.domain.User;
import org.blog.blogflatformproject.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/blog")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;
    private final UserService userService;
    private final BoardService boardService;

    //블로그이동
    @GetMapping("/{userId}")
    public String goBlog(@CookieValue(value="userId" , defaultValue = "") String userId, Model model){
        if(userId.isEmpty()){
            return "redirect:/user/loginform";
        }
        Long id = Long.parseLong(userId);
        Blog blog  = blogService.findByUserId(id);
        User user = userService.findByUserId(id);
        String userImage = user.getImagePath();

        List<Board> board = boardService.findByUserId(id);
        if(blog!=null){
            model.addAttribute("boardList" , board);
            model.addAttribute("blog" , blog);
            model.addAttribute("userImg",userImage);
            return "pages/blog/blog";
        }else{
            //블로그를 생성하지않았을 시 블로그생성화면으로이동
            return "pages/blog/blogform";
        }

    }

    //블로그생성창으로 이동
    @GetMapping("/blogform")
    public String goBlogForm(){
        return "pages/blog/blogform";
    }
    //블로그생성
    @PostMapping("/blogcreate")
    public String blogCreate(@ModelAttribute Blog blog , @CookieValue(value="userId" , defaultValue = "") String userId){

        if(blogService.saveBlog(blog , userId)!=null){
            return "redirect:/blog/"+userId;
        }else{
            return "redirect:/blog/blogform";
        }
    }
}
