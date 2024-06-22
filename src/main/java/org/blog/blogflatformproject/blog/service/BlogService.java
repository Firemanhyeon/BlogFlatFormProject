package org.blog.blogflatformproject.blog.service;

import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.blog.domain.Blog;
import org.blog.blogflatformproject.blog.repository.BlogRepository;
import org.blog.blogflatformproject.user.domain.User;
import org.blog.blogflatformproject.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    //해당유저의 블로그
    public Blog findByUserId(Long id){
        return blogRepository.findByUser_UserId(id);
    }

    //블로그생성
    public Blog saveBlog(Blog blog , String userId){
        User user = userRepository.findById(Long.parseLong(userId)).orElse(null);
        blog.setUser(user);
        blog.setBlogRegistrationDate(LocalDate.now());
        return blogRepository.save(blog);
    }

}
