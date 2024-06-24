package org.blog.blogflatformproject.user.controller;

import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.user.domain.User;
import org.blog.blogflatformproject.user.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    //중복체크
    @GetMapping("/duplicateChk")
    public boolean userDuplicateChk(@RequestParam("username") String username){
        if(userService.findByUserName(username)){
            return true;
        }else{
            return false;
        }
    }

    //쿠키에서 가져온 사용자아이디에 대한 사용자 정보 가져오기
    @GetMapping("/getUser")
    public String getUserWithCookie( @CookieValue(value="userId" , defaultValue = "") String logId ){
        if(logId.equals("")){
            return "noImg";
        }
        User user = userService.findByUserId(Long.parseLong(logId));
        return user.getImagePath();
    }
}
