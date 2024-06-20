package org.blog.blogflatformproject.user.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.user.domain.User;
import org.blog.blogflatformproject.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    //로그인창이동
    @GetMapping("/loginform")
    public String logInPage(){
        return "pages/user/loginForm";
    }
    //회원가입창이동
    @GetMapping("/signInForm")
    public String signIn(){
        return "pages/user/signInForm";
    }
    //회원가입
    @PostMapping("/userreg")
    public String userreg(@ModelAttribute User user){
        User user1 = userService.regUser(user);
        if(user1.getUserId()!=null){

            return "redirect:/user/welcome";
        }else{

            return "redirect:/user/error";
        }

    }
    //로그인 구현
    @PostMapping("/login")
    public String login(@ModelAttribute User user , HttpServletResponse response){
        User user1 = userService.login(user);
        if(user1!=null){
            Cookie cookie = new Cookie("userId" , user1.getUsername());
            cookie.setPath("/");
            response.addCookie(cookie);
            return null;
        }else{
            return "redirect:/user/error";
        }
    }
    //웰컴페이지이동
    @GetMapping("/welcome")
    public String goWelcomePage(){
        return "/pages/user/welcome";
    }
    //에러페이지이동
    @GetMapping("/error")
    public String goErrorPage(){
        return "/pages/user/error";
    }
    //로그인
}
