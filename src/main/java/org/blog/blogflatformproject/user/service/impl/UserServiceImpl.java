package org.blog.blogflatformproject.user.service.impl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.jwt.util.JwtTokenizer;
import org.blog.blogflatformproject.user.domain.Role;
import org.blog.blogflatformproject.user.domain.User;
import org.blog.blogflatformproject.user.dto.FileDto;
import org.blog.blogflatformproject.user.dto.UserLoginResponseDto;
import org.blog.blogflatformproject.user.repository.RoleRepository;
import org.blog.blogflatformproject.user.repository.UserRepository;
import org.blog.blogflatformproject.user.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    //로그인
    @Override
    public User login(User user){
        System.out.println("서비스호출");
        User foundUser = userRepository.findByUsername(user.getUsername());
        if(foundUser != null && passwordEncoder.matches(user.getPassword(), foundUser.getPassword())){
            return foundUser;
        } else {
            return null;
        }
    }
    //회원가입
    @Override
    public User regUser(User user) {
        Role userRole = roleRepository.findByRoleName("USER");
        // 사용자에게 역할 할당
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);
        user.setRegistrationDate(LocalDate.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    //아이디중복체크
    @Override
    public boolean checkDuplicated(String username){
        if(userRepository.findByUsername(username)==null){
            return false;
        }else{
            return true;
        }
    }
    //회원이름으로 회원정보 불러오기.
    @Override
    public User findByUserName(String username){
        return userRepository.findByUsername(username);
    }
    //회원아이디로 회원정보불러오기
    @Override
    public User findByUserId(Long userId){
        return userRepository.findById(userId).orElse(null);
    }
    //
    //파일 삭제
    @Override
    public boolean fileDelete(String imagePath){
        Path filePath = Paths.get(imagePath);
        try{
           return Files.deleteIfExists(filePath);
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }
    //파일저장
    @Transactional
    @Override
    public FileDto fileUpload(MultipartFile imageFile){
        if(!imageFile.isEmpty()){
            try{
                String uploadDir="/Users/jeonghohyeon/Desktop/blogUserImage";
                String fileName= UUID.randomUUID().toString();
                Path filePath = Paths.get(uploadDir+"/"+fileName);
                Files.copy(imageFile.getInputStream(),filePath);

                FileDto fileDTO = new FileDto();
                fileDTO.setImagePath(filePath.toString());
                fileDTO.setImageName(imageFile.getOriginalFilename());
                return fileDTO;
            }catch (IOException e){
                e.printStackTrace();
                return null;
            }
        }else{
            return null;
        }
    }
    //회원 이름 수정
    @Transactional
    @Override
    public User updateName(String name , String username){
        User user = findByUserName(username);
        if(user!=null){
            user.setName(name);
            return userRepository.save(user);
        }
        return null;
    }
    //imagePath 로 회원정보 불러오기
    @Override
    public User findByImgPath(String imgPath){
        return userRepository.findByImagePath(imgPath);
    }
    //회원정보저장,수정
    @Transactional
    @Override
    public User saveUser(User user){
        return userRepository.save(user);
    }
    //회원탈퇴
    @Transactional
    @Override
    public void deleteUser(String username){
        userRepository.deleteByUsername(username);
    }

    @Override
    public void addCookies(HttpServletResponse response, String accessToken, String refreshToken, User user) {
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(Math.toIntExact(JwtTokenizer.ACCESS_TOKEN_EXPIRE_COUNT / 1000));

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(Math.toIntExact(JwtTokenizer.REFRESH_TOKEN_EXPIRE_COUT / 1000));


        Cookie username = new Cookie("username",user.getUsername());
        username.setPath("/");
        username.setMaxAge(Math.toIntExact(JwtTokenizer.ACCESS_TOKEN_EXPIRE_COUNT / 1000));

        response.addCookie(username);
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }

    @Override
    public UserLoginResponseDto createLoginResponse(User user, String accessToken, String refreshToken) {
        return UserLoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getUserId())
                .userName(user.getUsername())
                .name(user.getName())
                .build();
    }

    @Override
    public String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public void deleteCookie(HttpServletResponse response){
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
    }
}
