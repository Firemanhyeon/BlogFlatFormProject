package org.blog.blogflatformproject.user.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.user.domain.SocialLoginInfo;
import org.blog.blogflatformproject.user.domain.User;
import org.blog.blogflatformproject.user.dto.FileDto;
import org.blog.blogflatformproject.user.service.SocialLoginInfoService;
import org.blog.blogflatformproject.user.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SocialLoginInfoService socialLoginInfoService;
    private final PasswordEncoder passwordEncoder;

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

    //소셜회원가입 페이지로 이동
    @GetMapping("/registerSocialUser")
    public String registerSocialUser(@RequestParam("provider") String provider , @RequestParam("socialId") String socialId
            , @RequestParam("uuid") String uuid , Model model){

        model.addAttribute("provider" , provider);
        model.addAttribute("socialId" , socialId);
        model.addAttribute("uuid" , uuid);

        return "/pages/user/registerSocialUser";

    }

    //소셜회원가입로직
    @PostMapping("/saveSocialUser")
    public String saveSocialUser(@RequestParam("provider")  String provider, @RequestParam("socialId")
    String socialId, @RequestParam("name")  String name, @RequestParam("username")  String username, @RequestParam("email")
                                 String email, @RequestParam("uuid")  String uuid,@RequestParam("imageFile")MultipartFile imageFile ,Model model) {
        Optional<SocialLoginInfo> socialLoginInfoOptional = socialLoginInfoService.findByProviderAndUuidAndSocialId(provider, uuid, socialId);

        if (socialLoginInfoOptional.isPresent()) {
            SocialLoginInfo socialLoginInfo = socialLoginInfoOptional.get();
            LocalDateTime now = LocalDateTime.now();
            Duration duration = Duration.between(socialLoginInfo.getCreatedAt(), now);

            if (duration.toMinutes() > 20) {
                return "redirect:/error"; // 20분 이상 경과한 경우 에러 페이지로 리다이렉트
            }
            FileDto dto=null;
            if(!imageFile.isEmpty()){
                //파일저장
                dto = userService.fileUpload(imageFile);
            }
            // 유효한 경우 User 정보를 저장합니다.
            userService.saveUser(username, name, email, socialId, provider,passwordEncoder,dto.getImageName(),dto.getImagePath());

            return "redirect:/user/loginform";
        } else {
            return "redirect:/error"; // 해당 정보가 없는 경우 에러 페이지로 리다이렉트
        }
    }
}
