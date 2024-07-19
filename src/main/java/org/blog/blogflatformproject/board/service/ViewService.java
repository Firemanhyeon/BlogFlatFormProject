package org.blog.blogflatformproject.board.service;

import org.blog.blogflatformproject.board.dto.BoardDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ViewService {
    void saveView(Long boardId , Long userId);
    List<BoardDTO> findByUserId(Long userId);
}
