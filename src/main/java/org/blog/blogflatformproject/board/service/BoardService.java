package org.blog.blogflatformproject.board.service;

import org.blog.blogflatformproject.board.domain.Board;
import org.blog.blogflatformproject.board.domain.Tag;
import org.blog.blogflatformproject.board.dto.BoardDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface BoardService {
    List<Board> getBoardListOrderByCreatedAt();
    Board addBoard(Board board , String username , Set<Tag> tags);
    Board updateBoard(Board board);
    List<Board> findByUsername(String username);
    Board findById(Long userId);
    String sanitizeBoardContent(String content);
    String extractFirstImageUrl(String content);
    boolean deleteBoard(Long boardId);
    void updateVisitCnt(Board brd,Long userId);
    List<BoardDTO> mySelectVal(int selectVal , String username);
    List<BoardDTO> getTemporaryAndOpenList(String username);
    List<BoardDTO> selectVal(int selectVal);

    List<BoardDTO> searchVal(String searchVal);
}
