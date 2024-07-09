package org.blog.blogflatformproject.board.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.blog.repository.BlogRepository;
import org.blog.blogflatformproject.board.domain.Board;
import org.blog.blogflatformproject.board.domain.Reply;
import org.blog.blogflatformproject.board.dto.ReplyDto;
import org.blog.blogflatformproject.board.repository.BoardRepository;
import org.blog.blogflatformproject.board.repository.ReplyRepository;
import org.blog.blogflatformproject.user.domain.User;
import org.blog.blogflatformproject.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    //댓글등록
    public Reply addReply(String replyContent , String boardId , Long userId){
        Board brd = boardRepository.findById(Long.parseLong(boardId)).orElse(null);
        Reply reply = new Reply();
        reply.setReplyContent(replyContent);
        reply.setBoard(brd);
        reply.setReplyCreated(LocalDateTime.now());
        reply.setUserId(userId);
        return replyRepository.save(reply);
    }

    //해당글의 댓글 가져오기
    public Page<ReplyDto> getReplies(Long boardId, Pageable pageable) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Board with id " + boardId + " not found"));

        Page<Reply> replies = replyRepository.findByBoardOrderByReplyCreatedDesc(board, pageable);
        return replies.map(this::convertToDto);
    }

    private ReplyDto convertToDto(Reply reply) {
        ReplyDto replyDto = new ReplyDto();
        replyDto.setReplyCreated(reply.getReplyCreated());
        replyDto.setReplyContent(reply.getReplyContent());
        replyDto.setPreReplyId(reply.getPreReplyId());
        replyDto.setReplyId(reply.getReplyId());

        User user = userRepository.findById(reply.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User with id " + reply.getUserId() + " not found"));

        replyDto.setUsername(user.getUsername());
        replyDto.setUserImgPath(user.getImagePath());

        return replyDto;
    }
}
