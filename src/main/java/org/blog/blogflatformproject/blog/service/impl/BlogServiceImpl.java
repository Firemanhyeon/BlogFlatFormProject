package org.blog.blogflatformproject.blog.service.impl;

import groovy.lang.Lazy;
import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.blog.domain.Blog;
import org.blog.blogflatformproject.blog.repository.BlogRepository;
import org.blog.blogflatformproject.blog.service.BlogService;
import org.blog.blogflatformproject.user.domain.User;
import org.blog.blogflatformproject.user.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {
    private final BlogRepository blogRepository;
    private final @Lazy UserService userService;

    //해당유저의 블로그
    @Override
    public Blog findByUsername(String username){
        return blogRepository.findByUser_Username(username);
    }

    //블로그아이디로 찾기
    @Override
    public Blog findById(Long blogId){
        return blogRepository.findById(blogId).orElse(null);
    }
    //블로그생성
    @Override
    public Blog saveBlog(Blog blog , String username){
        User user = userService.findByUserName(username);
        blog.setUser(user);
        blog.setBlogRegistrationDate(LocalDate.now());
        return blogRepository.save(blog);
    }
    //블로그이름수정
    @Override
    public Blog updateBlogName(Blog blog){
        return blogRepository.save(blog);
    }

}
