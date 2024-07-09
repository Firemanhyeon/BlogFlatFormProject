package org.blog.blogflatformproject.board.repository;

import org.blog.blogflatformproject.board.domain.Board;
import org.blog.blogflatformproject.board.domain.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRepository extends JpaRepository<Reply,Long> , PagingAndSortingRepository<Reply,Long> {
    Page<Reply> findByBoardOrderByReplyCreatedDesc(Board board, Pageable pageable);
}
