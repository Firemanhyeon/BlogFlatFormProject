package org.blog.blogflatformproject.board.repository;

import org.blog.blogflatformproject.board.domain.Board;
import org.blog.blogflatformproject.board.domain.Views;
import org.blog.blogflatformproject.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViewsRepository extends JpaRepository<Views,Long> {

    List<Views> findAllByUserOrderByViewCreatedDesc(User user);
    Views findByUserAndBoard(User user , Board board);
}
