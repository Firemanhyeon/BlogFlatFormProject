package org.blog.blogflatformproject.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.blog.blogflatformproject.user.domain.UserContext;
import org.blog.blogflatformproject.user.service.UserService;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Setter
public class AuthenticationFilter implements Filter {

    private UserService userService;
    // 인증이 필요하지 않은 URL을 저장하는 Set
    private static final Set<String> EXCLUDED_URLS = new HashSet<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 여기에 제외할 URL을 추가합니다.
        EXCLUDED_URLS.add("/");
        EXCLUDED_URLS.add("/user/loginform");
        EXCLUDED_URLS.add("/user/login");
        EXCLUDED_URLS.add("/user/signInForm");
        EXCLUDED_URLS.add("/user/userreg");
        EXCLUDED_URLS.add("/css/main.css");

        // 정적 리소스 경로 추가
        EXCLUDED_URLS.add("/css/*");

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain ) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = httpRequest.getRequestURI();
        System.out.println("필터실행");
        // 현재 요청이 인증이 필요하지 않은 URL인지 확인
        if (EXCLUDED_URLS.contains(requestURI)) {
            chain.doFilter(request, response);
            return;
        }
        //쿠키에서 userId를 추출
        String userId = getUserIdFromCookies(httpRequest.getCookies());
        System.out.println(userId);
        if(userId != null) {
            UserContext.setUserId(userId);
            try {
                chain.doFilter(request, response);
            } finally {
                UserContext.clear();
            }
        }else{
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/user/loginform");
        }

    }

    @Override
    public void destroy() {

    }
    private String validateTokenAndGetUserId(String token){
        //토큰 검증 및 사용자 Id 추출 로직

        return null;
    }

    private String getUserIdFromCookies(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userId".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
