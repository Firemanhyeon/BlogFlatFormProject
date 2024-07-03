package org.blog.blogflatformproject.blog.repository;

import org.blog.blogflatformproject.blog.domain.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<Blog,Long> {
    Blog findByUser_Username(String user_username);
}
