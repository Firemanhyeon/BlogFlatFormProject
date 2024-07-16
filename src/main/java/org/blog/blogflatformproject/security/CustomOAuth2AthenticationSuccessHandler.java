package org.blog.blogflatformproject.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.blog.blogflatformproject.jwt.util.JwtTokenizer;
import org.blog.blogflatformproject.user.domain.RefreshToken;
import org.blog.blogflatformproject.user.domain.Role;
import org.blog.blogflatformproject.user.domain.SocialLoginInfo;
import org.blog.blogflatformproject.user.domain.User;
import org.blog.blogflatformproject.user.dto.UserLoginResponseDto;
import org.blog.blogflatformproject.user.service.RefreshTokenService;
import org.blog.blogflatformproject.user.service.SocialLoginInfoService;
import org.blog.blogflatformproject.user.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

//로그인 성공 핸들러
@RequiredArgsConstructor
@Component
public class CustomOAuth2AthenticationSuccessHandler implements AuthenticationSuccessHandler{
    private final JwtTokenizer jwtTokenizer;
    private final RefreshTokenService refreshTokenService;
    private final SocialLoginInfoService socialLoginInfoService;
    private final UserService userService;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("로그인성공");
        // 요청 경로에서 provider 추출
        // redirect-uri: "{baseUrl}/login/oauth2/code/{registrationId}"
        String requestUri = request.getRequestURI();
        String provider = extractProviderFromUri(requestUri);
        // provider가 없는 경로 요청이 왔다는것은 문제가 발생한것이다.
        if(provider == null){
            System.out.println("provider없음");
            response.sendRedirect("/");
            return;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) auth.getPrincipal();

        // attributes 에 소셜로그인한 정보가 들어가 있는데, 소셜로그인 방식에따라서 key가 달라질 수 있다.
        // OAuth2 로그인 방식(provider)이 많아질때마다 조건문이 들어갈 수 있다.
        int socialId = (int)defaultOAuth2User.getAttributes().get("id");

        Optional<User> userOptional = userService.findByProviderAndSocialId(provider, String.valueOf(socialId));

        if (userOptional.isPresent()) {
            System.out.println("회원정보있음");
            // 회원 정보가 있으면 로그인 처리
            User user = userOptional.get();

            // CustomUserDetails 생성
            CustomUserDetails customUserDetails = new CustomUserDetails(user.getUsername(), user.getPassword(), user.getName(), user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList()));

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
            userService.addCookies(response , chkAccessToken,refreshToken,user);
            response.sendRedirect("/");


        }else { // 소셜로 아직 회원가입이 안되었을 때.
            // 소셜 로그인 정보 저장
            System.out.println("회원정보없음");
            SocialLoginInfo socialLoginInfo = socialLoginInfoService.saveSocialLoginInfo(provider, String.valueOf(socialId));
            response.sendRedirect("/user/registerSocialUser?provider=" + provider + "&socialId=" + socialId + "&uuid=" + socialLoginInfo.getUuid());
        }
    }


    private String extractProviderFromUri(String uri) {
        if(uri == null || uri.isBlank()) {
            return null;
        }

        if(!uri.startsWith("/login/oauth2/code/")){
            return null;
        }

        // 예: /login/oauth2/code/github -> github
        String[] segments = uri.split("/");
        return segments[segments.length - 1];
    }
}