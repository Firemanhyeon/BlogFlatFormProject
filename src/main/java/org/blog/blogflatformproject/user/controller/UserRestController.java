package org.blog.blogflatformproject.user.controller;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.blog.blogflatformproject.user.domain.User;
import org.blog.blogflatformproject.user.dto.FileDTO;
import org.blog.blogflatformproject.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    //이름 수정
    @PutMapping("/updatename")
    public ResponseEntity<String> updateName(@RequestParam("name") String name,
                                     @CookieValue(value="userId" , defaultValue = "") String logId){

        User user = userService.updateName(name , logId);
        if(user!=null){
            return ResponseEntity.ok(user.getName());
        }
        return ResponseEntity.status(500).body("수정실패");
    }

    //이미지수정
    @PostMapping("/updateImg")
    public ResponseEntity<FileDTO> updateImg(@RequestParam("imageFile") MultipartFile imageFile){
        System.out.println(imageFile);
        return null;
    }
}
