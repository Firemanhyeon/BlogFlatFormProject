package org.blog.blogflatformproject.board.service;

import org.blog.blogflatformproject.board.domain.Like;
import org.springframework.stereotype.Service;

@Service
public interface LikeService {
    Like findByBoardAndUser(Long boardId, Long userId);
    Like addLike(Long boardId,Long userId);
    void removeLike(Long boardId , Long userId);
}
