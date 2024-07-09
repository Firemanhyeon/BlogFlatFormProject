package org.blog.blogflatformproject.blog.controller;

import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.blog.domain.Blog;
import org.blog.blogflatformproject.blog.service.BlogService;
import org.blog.blogflatformproject.board.domain.Board;
import org.blog.blogflatformproject.board.service.BoardService;
import org.blog.blogflatformproject.jwt.util.JwtTokenizer;
import org.blog.blogflatformproject.user.domain.Follow;
import org.blog.blogflatformproject.user.domain.User;
import org.blog.blogflatformproject.user.service.FollowService;
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
    private final JwtTokenizer jwtTokenizer;
    private final FollowService followService;

    //블로그이동
    @GetMapping("/{username}")
    public String goBlog(@PathVariable("username") String username, Model model,
                         @CookieValue(value = "accessToken" , defaultValue = "") String accessToken){
        if(username.isEmpty()){
            return "redirect:/user/loginform";
        }
        Blog blog  = blogService.findByUsername(username);
        User user = userService.findByUserName(username);
        String userImage = user.getImagePath();
        //팔로우 했는지 안했는지 확인

        boolean isFollow;
        if(followService.chkFollow(user,userService.findByUserId(jwtTokenizer.getUserIdFromToken(accessToken)))){
            isFollow=true;
        }else{
            isFollow=false;
        }

        List<Board> board = boardService.findByUsername(username);
        if(blog!=null){
            model.addAttribute("boardList" , board);
            model.addAttribute("blog" , blog);
            model.addAttribute("userImg",userImage);
            model.addAttribute("username" , user.getUsername());
            model.addAttribute("isFollow",isFollow);
            return "pages/blog/blog";
        }else{
            //블로그를 생성하지않았을 시 블로그생성화면으로이동
            return "pages/blog/blogform";
        }
    }
    //내 블로그이동
    @GetMapping("/mypage")
    public String goMyBlog(@CookieValue(value = "username" , defaultValue = "") String username , Model model){
        if(username.isEmpty()){
            return "redirect:/user/loginform";
        }
        Blog blog  = blogService.findByUsername(username);
        User user = userService.findByUserName(username);
        String userImage = user.getImagePath();
        boolean isFollow=false;
        List<Board> board = boardService.findByUsername(username);
        if(blog!=null){
            model.addAttribute("boardList" , board);
            model.addAttribute("blog" , blog);
            model.addAttribute("userImg",userImage);
            model.addAttribute("username" , user.getUsername());
            model.addAttribute("isFollow",isFollow);
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
    public String blogCreate(@ModelAttribute Blog blog , @CookieValue(value="username" , defaultValue = "") String username){

        if(blogService.saveBlog(blog , username)!=null){
            return "redirect:/blog/"+username;
        }else{
            return "redirect:/blog/blogform";
        }
    }

    //유저 설정 페이지
    @GetMapping("/settings")
    public String myBlogSetting(@CookieValue(value="username" , defaultValue = "") String username , Model model ){
        //해당유저의 설정페이지로 이동하기
        System.out.println("설정페이지이동");
        User user = userService.findByUserName(username);
        Blog blog = blogService.findByUsername(username);
        model.addAttribute("user" , user);
        model.addAttribute("blog", blog);
        return "pages/user/userSetting";
    }
}
