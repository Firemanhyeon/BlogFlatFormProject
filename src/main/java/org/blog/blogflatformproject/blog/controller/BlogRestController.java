package org.blog.blogflatformproject.blog.controller;

import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.blog.domain.Blog;
import org.blog.blogflatformproject.blog.service.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public Long blogCheck(@CookieValue(value = "username", defaultValue = "") String username) {
        Blog blog = blogService.findByUsername(username);
        if (blog != null) {
            return blog.getBlogId();
        }
        return null;
    }

    @PutMapping("/updateBlogName")
    public ResponseEntity<String> updateBlogName(@RequestParam("blogName") String blogName,
                                                 @CookieValue(value = "username", defaultValue = "") String username) {
        Blog blog = blogService.findByUsername(username);
        if (blog != null) {
            blog.setBlogName(blogName);
            Blog updateBlogName = blogService.updateBlogName(blog);

            return new ResponseEntity(updateBlogName.getBlogName(), HttpStatus.OK);
        }
        return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
    }

}
