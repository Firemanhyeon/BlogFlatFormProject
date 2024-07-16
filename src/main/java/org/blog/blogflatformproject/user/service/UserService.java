package org.blog.blogflatformproject.user.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.blog.blogflatformproject.user.domain.User;
import org.blog.blogflatformproject.user.dto.FileDto;
import org.blog.blogflatformproject.user.dto.UserLoginResponseDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserService {

    User login(User user);
    User regUser(User user);
    boolean checkDuplicated(String username);
    User findByUserName(String username);
    User findByUserId(Long userId);
    boolean fileDelete(String imagePath);
    FileDto fileUpload(MultipartFile imageFile);
    User updateName(String name , String username);
    User findByImgPath(String imgPath);
    User saveUser(User user);
    void deleteUser(String username);
    void addCookies(HttpServletResponse response, String accessToken, String refreshToken, User user);
    UserLoginResponseDto createLoginResponse(User user, String accessToken, String refreshToken);
    String getCookieValue(HttpServletRequest request, String name);
    void deleteCookie(HttpServletResponse response);
    Optional<User> findByProviderAndSocialId(String provider,String socialId);
    User saveUser(String username, String name, String email, String socialId, String provider, PasswordEncoder passwordEncoder,String imageName, String imagePath);
}
