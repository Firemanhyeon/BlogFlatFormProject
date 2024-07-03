package org.blog.blogflatformproject.blog.controller;

import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.blog.domain.Blog;
import org.blog.blogflatformproject.blog.service.BlogService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor
public class BlogRestController {


    private final BlogService blogService;

//    @GetMapping("/info")
//    public Blog blogInfo(@CookieValue(value="userId" , defaultValue = "") String userId){
//        return null;
//    }

    @GetMapping("/check")
    public Long blogCheck(@CookieValue(value="username" , defaultValue = "") String username){
        Blog blog = blogService.findByUsername(username);
        System.out.println(blog.getBlogId());
        return blog.getBlogId();
    }
}
