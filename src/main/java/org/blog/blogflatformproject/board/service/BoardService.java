package org.blog.blogflatformproject.board.service;

import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.blog.domain.Blog;
import org.blog.blogflatformproject.blog.repository.BlogRepository;
import org.blog.blogflatformproject.board.domain.Board;
import org.blog.blogflatformproject.board.domain.Tag;
import org.blog.blogflatformproject.board.repository.BoardRepository;
import org.blog.blogflatformproject.board.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BoardService {
        private final BoardRepository boardRepository;
        private final BlogRepository blogRepository;


        public Board addBoard(Board board , String userId , Set<Tag> tags) {
            Blog blog = blogRepository.findByUser_UserId(Long.parseLong(userId));
            board.setTags(tags);
            board.setBlog(blog);
            board.setCreateAt(LocalDate.now());
            return boardRepository.save(board);
        }
        //해당유저의 글찾기
        public List<Board> findByUserId(Long userId){
            Blog blog = blogRepository.findByUser_UserId(userId);
            return boardRepository.findByBlog(blog);
        }
        //게시글번호로 글 정보 가져오기
    public Board findById(Long userId){
            return boardRepository.findById(userId).orElse(null);
    }
}
