package org.blog.blogflatformproject.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.jwt.exception.CustomAuthenticationEntryPoint;
import org.blog.blogflatformproject.jwt.filter.JwtAuthenticationFilter;
import org.blog.blogflatformproject.jwt.util.JwtTokenizer;
import org.blog.blogflatformproject.security.CustomLoginSuccessHandler;
import org.blog.blogflatformproject.security.CustomOAuth2AthenticationSuccessHandler;
import org.blog.blogflatformproject.security.CustomUserDetailService;
import org.blog.blogflatformproject.user.service.SocialUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomLoginSuccessHandler customLoginSuccessHandler;
    private final CustomUserDetailService customUserDetailService;
    private final SocialUserService socialUserService;
    private final CustomOAuth2AthenticationSuccessHandler customOAuth2AthenticationSuccessHandler;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtTokenizer jwtTokenizer,
                                                   CustomAuthenticationEntryPoint customAuthenticationEntryPoint)
            throws Exception {
        http
                .authorizeRequests(authorize -> authorize
                        .requestMatchers("/", "/user/loginform", "/user/signInForm", "/board/boardInfo/**", "/blog/**",
                                "/api/user/duplicateChk",
                                "/user/userreg", "/user/login", "/login", "/user/welcome", "/css/**", "/js/**",
                                "/Users/jeonghohyeon/Desktop/blogUserImage/**"
                                , "/api/user/login", "/api/user/getUser", "/api/user/updatename", "/user/logout",
                                "/board/boardInfo/**"
                                , "/board/getReplies", "/api/user/getFollowCnt", "/api/user/getFollowingCnt",
                                "/board/getSeriesInfo/**"
                                , "/board/selectVal", "board/mySelectVal", "/image/**", "/login/oauth2/code/github",
                                "/user/registerSocialUser/**"
                                , "/user/saveSocialUser", "/oauth2/**", "/api/user/auth/kakao/callback",
                                "/board/searchVal").permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenizer), UsernamePasswordAuthenticationFilter.class)
                .formLogin(form -> form.disable())
//                .logout(logout -> logout
//                        .logoutUrl("/user/logout")
//                        .logoutSuccessUrl("/")
//                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthenticationEntryPoint))
                .httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer.disable())
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/loginform")
                        .failureUrl("/")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(this.oauth2UserService()))
                        .successHandler(customOAuth2AthenticationSuccessHandler)
                );
        return http.build();
    }

    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowedMethods(List.of("GET", "POST", "DELETE", "PATCH", "OPTION", "PUT"));
        config.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        return oauth2UserRequest -> {
            OAuth2User oauth2User = delegate.loadUser(oauth2UserRequest);
            // 여기에 Github 유저 정보를 처리하는 로직을 추가할 수 있습니다.
            // 예: DB에 사용자 정보 저장, 권한 부여 등

            String token = oauth2UserRequest.getAccessToken().getTokenValue();

            // Save or update the user in the database
            String provider = oauth2UserRequest.getClientRegistration().getRegistrationId();
            String socialId = String.valueOf(oauth2User.getAttributes().get("id"));
            String username = (String) oauth2User.getAttributes().get("login");
            String email = (String) oauth2User.getAttributes().get("email");
            String avatarUrl = (String) oauth2User.getAttributes().get("avatar_url");

            socialUserService.saveOrUpdateUser(socialId, provider, username, email, avatarUrl);

            return oauth2User;
        };
    }
}
