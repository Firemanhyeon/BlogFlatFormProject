package org.blog.blogflatformproject.user.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.user.domain.User;
import org.blog.blogflatformproject.user.dto.FileDto;
import org.blog.blogflatformproject.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        if(!imageFile.isEmpty()){
            //파일저장
            FileDto dto = userService.fileUpload(imageFile);
            user.setImagePath(dto.getImagePath());
            user.setImageName(dto.getImageName());
        }
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
        userService.deleteCookie(response);
        return "redirect:/";
    }
    //회원탈퇴
    @PostMapping("/deleteUser")
    public String deleteUser(@CookieValue("username") String username,
                             HttpServletResponse response){
        userService.deleteUser(username);
        userService.deleteCookie(response);
        return "redirect:/";
    }
}
