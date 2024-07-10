package org.blog.blogflatformproject.board.repository;

import org.blog.blogflatformproject.blog.domain.Blog;
import org.blog.blogflatformproject.blog.domain.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeriesRepository extends JpaRepository<Series,Long> {
    List<Series> findAllByBlog(Blog blog);
}
