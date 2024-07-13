package org.blog.blogflatformproject.board.service.impl;

import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.board.domain.Board;
import org.blog.blogflatformproject.board.domain.Like;
import org.blog.blogflatformproject.board.repository.LikeRepository;
import org.blog.blogflatformproject.board.service.BoardService;
import org.blog.blogflatformproject.board.service.LikeService;
import org.blog.blogflatformproject.user.domain.User;
import org.blog.blogflatformproject.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final UserService userService;
    private final BoardService boardService;
    private final LikeRepository likeRepository;

    @Override
    public Like findByBoardAndUser(Long boardId,Long userId){
        Board brd = boardService.findById(boardId);
        User user = userService.findByUserId(userId);

        return likeRepository.findByBoardAndUser(brd,user);
    }

    @Transactional
    @Override
    public Like addLike(Long boardId,Long userId){
        Board brd = boardService.findById(boardId);
        User user = userService.findByUserId(userId);
        Like like = new Like();
        like.setBoard(brd);
        like.setUser(user);
        return likeRepository.save(like);
    }

    @Transactional
    @Override
    public void removeLike(Long boardId , Long userId){
        Like like = findByBoardAndUser(boardId,userId);
        likeRepository.deleteById(like.getLikeId());
    }
}
