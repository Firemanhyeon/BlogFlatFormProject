package org.blog.blogflatformproject.blog.service;

import org.blog.blogflatformproject.blog.domain.Blog;
import org.springframework.stereotype.Service;

@Service
public interface BlogService {

    Blog findByUsername(String username);
    Blog findById(Long blogId);
    Blog saveBlog(Blog blog , String username);
    Blog updateBlogName(Blog blog);

}
