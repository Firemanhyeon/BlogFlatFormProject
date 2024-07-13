package org.blog.blogflatformproject.board.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.board.domain.Board;
import org.blog.blogflatformproject.board.domain.Reply;
import org.blog.blogflatformproject.board.dto.ReplyDto;
import org.blog.blogflatformproject.board.repository.ReplyRepository;
import org.blog.blogflatformproject.board.service.BoardService;
import org.blog.blogflatformproject.board.service.ReplyService;
import org.blog.blogflatformproject.user.domain.User;
import org.blog.blogflatformproject.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {
    private final ReplyRepository replyRepository;
    private final BoardService boardService;
    private final UserService userService;

    //댓글등록
    @Override
    @Transactional
    public Reply addReply(String replyContent , String boardId , Long userId){
        Board brd = boardService.findById(Long.parseLong(boardId));
        Reply reply = new Reply();
        reply.setReplyContent(replyContent);
        reply.setBoard(brd);
        reply.setReplyCreated(LocalDateTime.now());
        reply.setUserId(userId);
        return replyRepository.save(reply);
    }

    //해당글의 댓글 가져오기
    @Override
    public Page<ReplyDto> getReplies(Long boardId, Pageable pageable) {
        Board board = boardService.findById(boardId);
        Page<Reply> replies = replyRepository.findByBoardAndPreReplyIdIsNullOrderByReplyCreatedDesc(board, pageable);
        return replies.map(this::convertToDto);
    }

    @Override
    public ReplyDto convertToDto(Reply reply) {
        ReplyDto replyDto = new ReplyDto();
        replyDto.setReplyCreated(reply.getReplyCreated());
        replyDto.setReplyContent(reply.getReplyContent());
        replyDto.setPreReplyId(reply.getPreReplyId());
        replyDto.setReplyId(reply.getReplyId());

        User user = userService.findByUserId(reply.getUserId());
        replyDto.setUsername(user.getUsername());
        replyDto.setUserImgPath(user.getImagePath());

        return replyDto;
    }

    //대댓글 등록
    @Override
    @Transactional
    public Reply addCommentReply(ReplyDto dto){
        Reply reply = new Reply();
        User user = userService.findByUserName(dto.getUsername());
        Board brd = boardService.findById(dto.getBoardId());
        reply.setPreReplyId(dto.getPreReplyId());
        reply.setReplyContent(dto.getReplyContent());
        reply.setReplyCreated(LocalDateTime.now());
        reply.setUserId(user.getUserId());
        reply.setBoard(brd);
        return replyRepository.save(reply);
    }
    //대댓글 불러오기
    @Override
    public List<ReplyDto> getCommentReplies(Long replyId){

        List<Reply> reply = replyRepository.findAllByPreReplyId(replyId);
        List<ReplyDto> dtos = new ArrayList<>();

        for(Reply rply : reply){
            ReplyDto dto = new ReplyDto();
            dto.setReplyId(rply.getReplyId());
            dto.setReplyContent(rply.getReplyContent());
            User user = userService.findByUserId(rply.getUserId());
            dto.setUsername(user.getUsername());
            dto.setUserImgPath(user.getImagePath());
            dto.setPreReplyId(replyId);
            dtos.add(dto);
        }
        return dtos;
    }
}
