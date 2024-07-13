package org.blog.blogflatformproject.board.service;

import org.blog.blogflatformproject.board.domain.Reply;
import org.blog.blogflatformproject.board.dto.ReplyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReplyService {
    Reply addReply(String replyContent , String boardId , Long userId);
    Page<ReplyDto> getReplies(Long boardId, Pageable pageable);
    ReplyDto convertToDto(Reply reply);
    Reply addCommentReply(ReplyDto dto);
    List<ReplyDto> getCommentReplies(Long replyId);
}
