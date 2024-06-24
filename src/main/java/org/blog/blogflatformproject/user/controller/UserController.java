package org.blog.blogflatformproject.user.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.user.domain.User;
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
        System.out.println("로그인폼 요청");
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
        if(!imageFile.isEmpty()){
            try{
                String uploadDir="/Users/jeonghohyeon/Desktop/blogUserImage";

                String fileName= UUID.randomUUID().toString();
                Path filePath = Paths.get(uploadDir+"/"+fileName);
                Files.copy(imageFile.getInputStream(),filePath);

                user.setImagePath(filePath.toString());
                user.setImageName(imageFile.getOriginalFilename());
            }catch (IOException e){
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("message" , "이미지 업로드에 실패했습니다.");
                return "redirect:/user/error";
            }
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
    //로그인 구현
    @PostMapping("/login")
    public String login(@ModelAttribute User user , HttpServletResponse response , RedirectAttributes redirectAttributes){
        User user1 = userService.login(user);
        if(user1!=null){
            Cookie cookie = new Cookie("userId" , user1.getUserId().toString());
            cookie.setPath("/");
            response.addCookie(cookie);
            return "redirect:/blog/"+user1.getUserId();
        }else{
            redirectAttributes.addFlashAttribute("message", "로그인실패");
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
    @GetMapping("/logout")
    public String logOut(HttpServletResponse response){
        Cookie cookie = new Cookie("userId" , null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/";
    }
}
