//package org.blog.blogflatformproject.config;
//
//import org.blog.blogflatformproject.filter.AuthenticationFilter;
//import org.blog.blogflatformproject.user.service.UserService;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class FilterConfig {
//
//    @Bean
//    public FilterRegistrationBean<AuthenticationFilter> authenticationFilter(UserService userService){
//        FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
//        AuthenticationFilter authenticationFilter = new AuthenticationFilter();
//
//        authenticationFilter.setUserService(userService);//userService 주입
//
//        registrationBean.setFilter(authenticationFilter);
//        registrationBean.addUrlPatterns("/blog/blogform");
//        registrationBean.addUrlPatterns("/blog/blogcreate");
//        registrationBean.addUrlPatterns("/blog/settings");
//        registrationBean.addUrlPatterns("/board/boardform");
//
//        registrationBean.setOrder(1);
//
//        return registrationBean;
//    }
//}
