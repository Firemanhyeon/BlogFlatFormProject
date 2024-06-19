package org.blog.blogflatformproject.user.controller;

import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.user.domain.User;
import org.blog.blogflatformproject.user.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @GetMapping("/duplicateChk")
    public boolean userDuplicateChk(@RequestParam("username") String username){
        if(userService.findByUserName(username)){
            return true;
        }else{
            return false;
        }
    }
}
