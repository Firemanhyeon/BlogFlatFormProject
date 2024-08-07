package org.blog.blogflatformproject.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.security.CustomUserDetails;
import org.blog.blogflatformproject.user.domain.*;
import org.blog.blogflatformproject.jwt.util.JwtTokenizer;
import org.blog.blogflatformproject.user.dto.FileDto;
import org.blog.blogflatformproject.user.dto.UserLoginDto;
import org.blog.blogflatformproject.user.dto.UserLoginResponseDto;
import org.blog.blogflatformproject.user.service.FollowService;
import org.blog.blogflatformproject.user.service.RefreshTokenService;
import org.blog.blogflatformproject.user.service.UserService;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenizer jwtTokenizer;
    private final RefreshTokenService refreshTokenService;
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
                refreshTokenService.deleteRefreshToken(user.getUserId());
                //리프레시토큰 생성
                refreshTokenService.addRefreshToken(refreshTokenEntity);

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

    //카카오로그인
    @GetMapping("/auth/kakao/callback")
    public void kakaoLogin(@RequestParam("code") String code ,HttpServletResponse httpServletResponse){
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type","application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "289b00ad2e01aaf2f3cbf209a97a53ed");
        params.add("redirect_uri", "http://localhost:8080/api/user/auth/kakao/callback");
        params.add("code", code);


        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token", // https://{요청할 서버 주소}
                HttpMethod.POST, // 요청할 방식
                kakaoTokenRequest, // 요청할 때 보낼 데이터
                String.class // 요청 시 반환되는 데이터 타입
        );

        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oAuthToken=null;
        try {
            oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        System.out.println("발급받은 액세스키: "+oAuthToken.getAccess_token());
        RestTemplate rt2 = new RestTemplate();
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization","Bearer "+oAuthToken.getAccess_token());
        headers2.add("Content-Type","application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 = new HttpEntity<>(params,headers2);

        ResponseEntity<String> response2 = rt2.exchange(
                "https://kapi.kakao.com/v2/user/me", // https://{요청할 서버 주소}
                HttpMethod.POST, // 요청할 방식
                kakaoProfileRequest2, // 요청할 때 보낼 데이터
                String.class // 요청 시 반환되는 데이터 타입
        );


        ObjectMapper objectMapper2 = new ObjectMapper();
        KakaoLoginInfo kakaoLoginInfo=null;
        try {
            kakaoLoginInfo = objectMapper2.readValue(response2.getBody(), KakaoLoginInfo.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(kakaoLoginInfo.getNickname());
        System.out.println(kakaoLoginInfo.getId());
        System.out.println(kakaoLoginInfo.getEmail());
        System.out.println(kakaoLoginInfo.getConnectedAt());

        Optional<User> userOptional = userService.findByProviderAndSocialId("kakao", kakaoLoginInfo.getId());

        if(userOptional.isPresent()){
            //이미 가입한 아이디가 있는경우
            //액세스토큰 리프레스토큰 발급하고 로그인처리
            System.out.println("회원정보있음");
            // 회원 정보가 있으면 로그인 처리
            User user = userOptional.get();

            // CustomUserDetails 생성
            org.blog.blogflatformproject.security.CustomUserDetails customUserDetails = new CustomUserDetails(user.getUsername(), user.getPassword(), user.getName(), user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList()));

            // Authentication 객체 생성 및 SecurityContext에 설정
            Authentication newAuth = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(newAuth);

            String refreshToken = jwtTokenizer.createRefreshToken(user.getUserId(), user.getEmail(), user.getName(), user.getUsername(), user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList()));
            String chkAccessToken = jwtTokenizer.createAccessToken(user.getUserId(), user.getEmail(), user.getName(), user.getUsername(), user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList()));

            //데이터베이스에 넣을 객체 생성
            RefreshToken refreshTokenEntity = new RefreshToken();
            refreshTokenEntity.setValue(refreshToken);
            refreshTokenEntity.setUserId(user.getUserId());
            //기존에 저장되어있던 리프레시토큰 삭제
            refreshTokenService.deleteRefreshToken(user.getUserId());
            //리프레시토큰 생성
            refreshTokenService.addRefreshToken(refreshTokenEntity);

            UserLoginResponseDto loginResponse = userService.createLoginResponse(user,chkAccessToken,refreshToken);
            userService.addCookies(httpServletResponse , chkAccessToken,refreshToken,user);

            try {
                httpServletResponse.sendRedirect("/blog/"+user.getUsername());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            //가입한 아이디가 없는경우
            //소셜로그인 회원가입창으로 redirect
            try {
                httpServletResponse.sendRedirect("/user/registerSocialUser?provider=kakao&socialId=" + kakaoLoginInfo.getId() + "&uuid=uuid");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
