package org.blog.blogflatformproject.board.repository;

import org.blog.blogflatformproject.board.domain.Board;
import org.blog.blogflatformproject.board.domain.Like;
import org.blog.blogflatformproject.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like,Long> {
    Like findByBoardAndUser(Board board, User user);
}
