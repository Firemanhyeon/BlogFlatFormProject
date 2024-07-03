package org.blog.blogflatformproject.config;

import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.security.CustomLoginSuccessHandler;
import org.blog.blogflatformproject.security.CustomUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomLoginSuccessHandler customLoginSuccessHandler;
    private final CustomUserDetailService customUserDetailService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorize -> authorize
                        .requestMatchers("/", "/user/loginform", "/user/signInForm", "/board/boardInfo/**", "/blog/**", "/user/duplicateChk",
                                "/user/userreg", "/user/login","/login", "/user/welcome", "/css/**", "/js/**","/Users/jeonghohyeon/Desktop/blogUserImage/**","/user/getUser").permitAll()
                        .requestMatchers("/board/addboard" , "/blog//settings").hasRole("USER")
                        .anyRequest()
                        .authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/user/loginform")
                        .loginProcessingUrl("/login")
                        .successHandler(customLoginSuccessHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/user/logout")
                        .logoutSuccessUrl("/")
                )
                .csrf(csrf -> csrf.disable())
                .userDetailsService(customUserDetailService);
        return http.build();
    }

    //비밀번호 암호화해서 넣기
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
