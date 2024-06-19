package org.blog.blogflatformproject.user.controller;

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
    public String userreg(@RequestBody User user){
        System.out.println(user);
        User user1 = userService.regUser(user);
        if(user1.getUserId()!=null){

            return "redirect:/pages/user/welcome";
        }else{

            return "redirect:/pages/user/error";
        }

    }
    //로그인
}
