package org.blog.blogflatformproject.user.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.user.domain.User;
import org.blog.blogflatformproject.user.dto.FileDTO;
import org.blog.blogflatformproject.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

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
    public String userreg(@ModelAttribute User user ,
                          @RequestParam("imageFile")MultipartFile imageFile,
                          RedirectAttributes redirectAttributes){
        //파일저장
        FileDTO dto = userService.fileUpload(imageFile);
        user.setImagePath(dto.getImagePath());
        user.setImageName(dto.getImageName());

        //회원가입
        User user1 = userService.regUser(user);

        //회원가입이 완료됐다면
        if(user1.getUserId()!=null){
            return "redirect:/user/welcome";
        }else{
            redirectAttributes.addFlashAttribute("message" , "회원등록에 실패했습니다.");
            return "redirect:/user/error";
        }

    }
    //로그인 구현
//    @PostMapping("/login")
//    public String login(@ModelAttribute User user , HttpServletResponse response , RedirectAttributes redirectAttributes){
//        User user1 = userService.login(user);
//        System.out.println(user1);
//
//        if(user1!=null){
//            System.out.println("로그인완료");
//            Cookie cookie = new Cookie("userId" , user1.getUserId().toString());
//            cookie.setPath("/");
//            response.addCookie(cookie);
//            return "redirect:/blog/"+user1.getUserId();
//        }else{
//            System.out.println("로그인실패");
//            redirectAttributes.addFlashAttribute("message", "로그인실패");
//            return "redirect:/user/error";
//        }
//    }
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

    //로그아웃
    @GetMapping("/logout")
    public String logOut(HttpServletResponse response){
        System.out.println("로그아웃");
        Cookie cookie = new Cookie("username" , null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        Cookie accessToken = new Cookie("accessToken" , null);
        accessToken.setMaxAge(0);
        accessToken.setPath("/");

        Cookie refreshToken = new Cookie("refreshToken",null);
        refreshToken.setMaxAge(0);
        refreshToken.setPath("/");

        response.addCookie(refreshToken);
        response.addCookie(cookie);
        response.addCookie(accessToken);



        return "redirect:/";
    }
}
