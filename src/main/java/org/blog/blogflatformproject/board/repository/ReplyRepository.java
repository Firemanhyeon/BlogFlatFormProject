package org.blog.blogflatformproject.board.repository;

import org.blog.blogflatformproject.board.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRepository extends JpaRepository<Reply,Long> {
}
