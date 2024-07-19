package org.blog.blogflatformproject.board.service.impl;

import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.board.domain.Board;
import org.blog.blogflatformproject.board.domain.Views;
import org.blog.blogflatformproject.board.dto.BoardDTO;
import org.blog.blogflatformproject.board.event.CustomEventListener;
import org.blog.blogflatformproject.board.repository.ViewsRepository;
import org.blog.blogflatformproject.board.service.BoardService;
import org.blog.blogflatformproject.board.service.ViewService;
import org.blog.blogflatformproject.user.domain.User;
import org.blog.blogflatformproject.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ViewServiceImpl implements ViewService {
    private final ViewsRepository viewsRepository;
    private final BoardService boardService;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(CustomEventListener.class);

    //방문기록저장
    @Transactional
    @Override
    public void saveView(Long boardId , Long userId){
        Board brd = boardService.findById(boardId);
        User user = userService.findByUserId(userId);
        Views view = viewsRepository.findByUserAndBoard(user,brd);
        System.out.println(view);
        if(view==null){
            Views views = new Views();
            views.setViewCreated(LocalDateTime.now());
            views.setUser(user);
            views.setBoard(brd);
            viewsRepository.save(views);
        }else{
            view.setViewCreated(LocalDateTime.now());
            viewsRepository.save(view);
        }


    }

    //방문한 board 가져오기
    @Override
    public List<BoardDTO> findByUserId(Long userId){
        List<Views> list =viewsRepository.findAllByUserOrderByViewCreatedDesc(userService.findByUserId(userId));
        List<BoardDTO> dto = new ArrayList<>();
        for(Views view:list){
            Board board = boardService.findById(view.getBoard().getBoardId());
            BoardDTO boardDto = new BoardDTO();
            boardDto.setBoardTitle(board.getBoardTitle());
            boardDto.setBoardId(board.getBoardId());
            boardDto.setFirstImagePath(board.getFirstImagePath());
            dto.add(boardDto);
        }
        return dto;
    }
}
