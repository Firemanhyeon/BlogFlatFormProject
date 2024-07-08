package org.blog.blogflatformproject.board.service;

import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.board.domain.Board;
import org.blog.blogflatformproject.board.domain.Reply;
import org.blog.blogflatformproject.board.repository.BoardRepository;
import org.blog.blogflatformproject.board.repository.ReplyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;
    //댓글등록
    public Reply addReply(String replyContent , String boardId){
        Board brd = boardRepository.findById(Long.parseLong(boardId)).orElse(null);
        Reply reply = new Reply();
        reply.setReplyContent(replyContent);
        reply.setBoard(brd);
        reply.setReplyCreated(LocalDateTime.now());
        return replyRepository.save(reply);
    }
}
