package org.blog.blogflatformproject.user.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.user.domain.RefreshToken;
import org.blog.blogflatformproject.jwt.util.JwtTokenizer;
import org.blog.blogflatformproject.user.domain.Role;
import org.blog.blogflatformproject.user.domain.User;
import org.blog.blogflatformproject.user.dto.FileDTO;
import org.blog.blogflatformproject.user.dto.UserLoginDto;
import org.blog.blogflatformproject.user.dto.UserLoginResponseDto;
import org.blog.blogflatformproject.user.service.RefreshTokenService;
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
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid UserLoginDto loginDto , BindingResult bindingResult , HttpServletResponse httpServletResponse){
        //username , password가  null일때 혹은 정해진 형식에 맞지않을때
        System.out.println("로그인탔어요");
        if(bindingResult.hasErrors()){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        //해당 username으로 유저정보 가져와서 아이디비밀번호 맞는지 체크 맞지않다면 return
        User user = userService.findByUserName(loginDto.getUsername());
        if(!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        //여기까지체크한건 아이디비밀번호 일치하는지 체크. 아이디비밀번호맞음.

        List<String> roles = user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList()); //Role타입의 set에서 role의 이름만 들어있는 list가 필요함

        //액세스토큰과 리프레시토큰 생성
        String accessToken = jwtTokenizer.createAccessToken(user.getUserId(),user.getEmail(),user.getName(),user.getUsername(),roles);
        String refreshToken = jwtTokenizer.createRefreshToken(user.getUserId(),user.getEmail(),user.getName(),user.getUsername(),roles);

        //리프레시토큰 같은 경우엔 디비에 넣어줘야하니까 객체생성해서 넣어줌.
        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setValue(refreshToken);
        refreshTokenEntity.setUserId(user.getUserId());
        refreshTokenService.addRefreshToken(refreshTokenEntity);

        //응답을 위한 dto에 로그인정보 담아주기
        UserLoginResponseDto loginResponse = UserLoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getUserId())
                .name(user.getName())
                .build();

        //쿠키에 액세스토큰 담아주기
        Cookie accessTokenCookie = new Cookie("accessToken",accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(Math.toIntExact(JwtTokenizer.ACCESS_TOKEN_EXPIRE_COUNT/1000));//30분 30분이 다되면 refresh토큰 가지고 다시 요청해서 맞으면 새로운 accesstoken 발급해준다.

        //쿠키에 리프레시토큰 담아주기
        Cookie refreshTokenCookie = new Cookie("refreshToken" , refreshToken);
        refreshTokenCookie.setHttpOnly(true);//보안. (쿠키값을 js곳에서 접근할 수 없다.)
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(Math.toIntExact(JwtTokenizer.REFRESH_TOKEN_EXPIRE_COUT/1000));//유지시간. 7일 / 쿠키의 유지시간은 초단위인데 JWT의 시간단위는 밀리세컨드라 나눠줌.

        //서블릿응답에 보내기
        httpServletResponse.addCookie(accessTokenCookie);
        httpServletResponse.addCookie(refreshTokenCookie);

        //엔티티에 로그인정보담아서 return
        return new ResponseEntity(loginResponse , HttpStatus.OK);
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
    public ResponseEntity<String> updateName(@RequestParam("name") String name,
                                     @CookieValue(value="username" , defaultValue = "") String logId){

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
