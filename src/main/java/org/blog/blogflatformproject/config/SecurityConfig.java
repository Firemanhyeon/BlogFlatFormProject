package org.blog.blogflatformproject.config;

import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.jwt.exception.CustomAuthenticationEntryPoint;
import org.blog.blogflatformproject.jwt.filter.JwtAuthenticationFilter;
import org.blog.blogflatformproject.jwt.util.JwtTokenizer;
import org.blog.blogflatformproject.security.CustomLoginSuccessHandler;
import org.blog.blogflatformproject.security.CustomUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomLoginSuccessHandler customLoginSuccessHandler;
    private final CustomUserDetailService customUserDetailService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtTokenizer jwtTokenizer, CustomAuthenticationEntryPoint customAuthenticationEntryPoint) throws Exception {
        http
                .authorizeRequests(authorize -> authorize
                        .requestMatchers("/", "/user/loginform", "/user/signInForm", "/board/boardInfo/**", "/blog/**", "/user/duplicateChk",
                                "/user/userreg", "/user/login","/login", "/user/welcome", "/css/**", "/js/**","/Users/jeonghohyeon/Desktop/blogUserImage/**"
                                ,"/user/getUser","/api/user/login").permitAll()
                        .requestMatchers("/board/addboard" , "/blog//settings").hasRole("USER")
                        .anyRequest()
                        .authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenizer), UsernamePasswordAuthenticationFilter.class)
                .formLogin(form-> form.disable())
                .logout(logout -> logout
                        .logoutUrl("/user/logout")
                        .logoutSuccessUrl("/")
                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic->httpBasic.disable())
                .cors(cors-> cors.configurationSource(corsConfigurationSource()))
                .exceptionHandling(exception->exception
                        .authenticationEntryPoint(customAuthenticationEntryPoint));
                //.userDetailsService(customUserDetailService);
        return http.build();
    }

    public CorsConfigurationSource corsConfigurationSource(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowedMethods(List.of("GET" , "POST" , "DELETE" , "PATCH" , "OPTION" , "PUT"));
        source.registerCorsConfiguration("/**",config);
        return source;
    }
    //비밀번호 암호화해서 넣기
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
