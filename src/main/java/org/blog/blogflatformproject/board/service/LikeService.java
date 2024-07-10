package org.blog.blogflatformproject.board.service;

import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.blog.repository.BlogRepository;
import org.blog.blogflatformproject.board.domain.Board;
import org.blog.blogflatformproject.board.domain.Like;
import org.blog.blogflatformproject.board.repository.BoardRepository;
import org.blog.blogflatformproject.board.repository.LikeRepository;
import org.blog.blogflatformproject.user.domain.User;
import org.blog.blogflatformproject.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final LikeRepository likeRepository;

    public Like findByBoardAndUser(Long boardId,Long userId){
        Board brd = boardRepository.findById(boardId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);

        return likeRepository.findByBoardAndUser(brd,user);
    }

    @Transactional
    public Like addLike(Long boardId,Long userId){
        Board brd = boardRepository.findById(boardId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        Like like = new Like();
        like.setBoard(brd);
        like.setUser(user);
        return likeRepository.save(like);
    }

    @Transactional
    public void removeLike(Long boardId , Long userId){
        Like like = findByBoardAndUser(boardId,userId);
        likeRepository.deleteById(like.getLikeId());
    }
}
