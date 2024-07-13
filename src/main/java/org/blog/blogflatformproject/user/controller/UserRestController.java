package org.blog.blogflatformproject.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.user.domain.Follow;
import org.blog.blogflatformproject.user.domain.RefreshToken;
import org.blog.blogflatformproject.jwt.util.JwtTokenizer;
import org.blog.blogflatformproject.user.domain.Role;
import org.blog.blogflatformproject.user.domain.User;
import org.blog.blogflatformproject.user.dto.FileDto;
import org.blog.blogflatformproject.user.dto.UserLoginDto;
import org.blog.blogflatformproject.user.dto.UserLoginResponseDto;
import org.blog.blogflatformproject.user.service.FollowService;
import org.blog.blogflatformproject.user.service.impl.RefreshTokenServiceImpl;
import org.blog.blogflatformproject.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenizer jwtTokenizer;
    private final RefreshTokenServiceImpl refreshTokenServiceImpl;
    private final FollowService followService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid UserLoginDto loginDto , BindingResult bindingResult ,HttpServletRequest request ,  HttpServletResponse response){
        //username , password가  null일때 혹은 정해진 형식에 맞지않을때
        System.out.println("로그인탔어요");
        if(bindingResult.hasErrors()){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        //해당 username으로 유저정보 가져와서 아이디비밀번호 맞는지 체크 맞지않다면 return
        User user = userService.findByUserName(loginDto.getUsername());
        if(user==null){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        if(!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        //여기까지체크한건 아이디비밀번호 일치하는지 체크. 아이디비밀번호맞음.

        List<String> roles = user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList()); //Role타입의 set에서 role의 이름만 들어있는 list가 필요함

        //액세스토큰 확인. 있을 시 해당 액세스토큰에 대한 유저정보 반환.
        String chkAccessToken = userService.getCookieValue(request, "accessToken");
        if(chkAccessToken != null && jwtTokenizer.validateAccessToken(chkAccessToken)){
            System.out.println("액세스토큰이 있어요");
            UserLoginResponseDto loginResponse = userService.createLoginResponse(user,chkAccessToken,null);
            return new ResponseEntity(loginResponse,HttpStatus.OK);
        }else{
            //없을 시 리프레시토큰 비교
            System.out.println("액세스토큰이 없어요");
            String refreshToken = userService.getCookieValue(request,"refreshToken");
            //리프레시토큰이 있는경우
            //새로운 액세스 토큰을 생성하고 액세스토큰과 리프레시토큰을 반환.
            if(refreshToken!=null && jwtTokenizer.validateRefreshToken(refreshToken)){
                System.out.println("리프레시토큰이 있어요");
                chkAccessToken = jwtTokenizer.createAccessToken(user.getUserId(),user.getEmail(),user.getName(),user.getUsername(),roles);
                UserLoginResponseDto loginResponse = userService.createLoginResponse(user, chkAccessToken, refreshToken);
                userService.addCookies(response , chkAccessToken , refreshToken,user);
                return new ResponseEntity(loginResponse , HttpStatus.OK);
            }else{
                System.out.println("리프레시토큰이 없어요");
                //리프레시토큰이 없는경우

                //리프레시토큰과 액세스토큰 생성.
                refreshToken = jwtTokenizer.createRefreshToken(user.getUserId(), user.getEmail(), user.getName(), user.getUsername(), user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList()));
                chkAccessToken = jwtTokenizer.createAccessToken(user.getUserId(), user.getEmail(), user.getName(), user.getUsername(), user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList()));

                //데이터베이스에 넣을 객체 생성
                RefreshToken refreshTokenEntity = new RefreshToken();
                refreshTokenEntity.setValue(refreshToken);
                refreshTokenEntity.setUserId(user.getUserId());
                //기존에 저장되어있던 리프레시토큰 삭제
                refreshTokenServiceImpl.deleteRefreshToken(user.getUserId());
                //리프레시토큰 생성
                refreshTokenServiceImpl.addRefreshToken(refreshTokenEntity);

                UserLoginResponseDto loginResponse = userService.createLoginResponse(user,chkAccessToken,refreshToken);
                userService.addCookies(response , chkAccessToken,refreshToken,user);

                return new ResponseEntity<>(loginResponse, HttpStatus.OK);
            }
        }
    }


    //중복체크
    @GetMapping("/duplicateChk")
    public boolean userDuplicateChk(@RequestParam("username") String username){
        if(userService.checkDuplicated(username)){
            return true;
        }else{
            return false;
        }
    }

    //쿠키에서 가져온 사용자아이디에 대한 사용자 정보 가져오기
    @GetMapping("/getUser")
    public String getUserWithCookie( @CookieValue(value="username" , defaultValue = "") String username ){
        if(username.equals("")){
            return "noImg";
        }
        User user = userService.findByUserName(username);
        return user.getImagePath();
    }

    //이름 수정
    @PutMapping("/updatename")
    public ResponseEntity<String> updateName(@RequestParam("name") String name, @CookieValue(value="username" , defaultValue = "") String logId){
        User user = userService.updateName(name , logId);
        if(user!=null){
            return ResponseEntity.ok(user.getName());
        }
        return ResponseEntity.status(500).body("수정실패");
    }

    //이미지수정
    @PostMapping("/updateImg")
    public ResponseEntity<String> updateImg(@RequestParam("imageFile") MultipartFile imageFile,
                                             @RequestParam("beforeImg")String beforeImg,
                                            @CookieValue(value="username" , defaultValue = "") String username){
        User user = userService.findByImgPath(beforeImg);
        //기존 프로필사진이 있을경우 로컬삭제
        if(user!=null){
            //기존 유저 프로필사진삭제
            userService.fileDelete(beforeImg);
        }else{
            user = userService.findByUserName(username);
        }
        //프로필사진 파일저장
        FileDto file = userService.fileUpload(imageFile);
        //유저정보 파일경로 user테이블에서 수정

        if(file!=null){
            user.setImageName(file.getImageName());
            user.setImagePath(file.getImagePath());
            User updateUser = userService.saveUser(user);
            return new ResponseEntity<>(updateUser.getImagePath(),HttpStatus.OK);
        }

        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }

    //이메일변경
    @PutMapping("/updateEmail")
    public ResponseEntity<String> updateUserEmail(@RequestParam("email") String email,
                                                  @CookieValue(value = "username" ,  defaultValue = "") String username){
        User user = userService.findByUserName(username);
        if(user!=null){
            user.setEmail(email);
            User updateUser = userService.saveUser(user);
            return new ResponseEntity(updateUser.getEmail(),HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);

    }

    //이메일수신동의 변경
    @PutMapping("/updateEmailStatus")
    public ResponseEntity<String> updateUserEmailStatus(@RequestParam("emailStatus") boolean emailStatus,
                                                        @CookieValue(value = "username", defaultValue = "") String username){
        User user = userService.findByUserName(username);
        if(user!=null){
            user.setEmailStatus(emailStatus);
            User updateUser = userService.saveUser(user);
            return new ResponseEntity(updateUser.isEmailStatus(),HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }

    //팔로우
    @PostMapping("/follow")
    public ResponseEntity follow(@RequestParam("follower") String followedUsername, @CookieValue("accessToken") String accessToken){


        Long followingId = jwtTokenizer.getUserIdFromToken(accessToken);
        Long followedId = userService.findByUserName(followedUsername).getUserId();
        Follow follow = followService.getFollow(followingId,followedId);
        if(follow!=null){
            return ResponseEntity.ok().build();
        }
        return new ResponseEntity(null,HttpStatus.BAD_REQUEST);
    }
    //언팔로우
    @DeleteMapping("/unfollow")
    public void unfollow(@RequestParam("follower") String unfollowedUsername, @CookieValue("accessToken") String accessToken){
        Long followingId = jwtTokenizer.getUserIdFromToken(accessToken);
        Long followedId = userService.findByUserName(unfollowedUsername).getUserId();

        if(followingId!=null && followedId!=null){
            followService.getUnfollow(followingId , followedId);
        }
    }



    //팔로우수 불러오기
    @GetMapping("/getFollowCnt")
    public ResponseEntity getFollowerCnt(@RequestParam("blogUserName") String blogUserName){
        User user = userService.findByUserName(blogUserName);
        int followCount = followService.getFollowerCnt(user.getUserId());

        return new ResponseEntity(followCount,HttpStatus.OK);
    }
    //팔로잉수 불러오기
    @GetMapping("/getFollowingCnt")
    public ResponseEntity getFollowingCnt(@RequestParam("blogUserName")String blogUserName){
        User user = userService.findByUserName(blogUserName);
        int follwingCount = followService.getFollowingCnt(user.getUserId());

        return new ResponseEntity(follwingCount,HttpStatus.OK);
    }


}
