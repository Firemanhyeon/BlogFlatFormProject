package org.blog.blogflatformproject.board.repository;

import org.blog.blogflatformproject.blog.domain.Blog;
import org.blog.blogflatformproject.board.domain.Board;
import org.blog.blogflatformproject.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board,Long> {
    public List<Board> findByBlog(Blog blog);
}
