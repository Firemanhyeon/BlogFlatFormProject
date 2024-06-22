package org.blog.blogflatformproject.board.service;

import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.blog.domain.Blog;
import org.blog.blogflatformproject.blog.repository.BlogRepository;
import org.blog.blogflatformproject.board.domain.Board;
import org.blog.blogflatformproject.board.repository.BoardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
        private final BoardRepository boardRepository;
        private final BlogRepository blogRepository;

        public Board addBoard(Board board , String userId) {
            Blog blog = blogRepository.findByUser_UserId(Long.parseLong(userId));
            board.setBlog(blog);
            return boardRepository.save(board);
        }
        //해당유저의 글찾기
        public List<Board> findByUserId(Long userId){
            Blog blog = blogRepository.findByUser_UserId(userId);
            return boardRepository.findByBlog(blog);
        }
}
