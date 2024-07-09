package org.blog.blogflatformproject.board.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ReplyDto {
    private Long replyId;
    private String replyContent;
    private LocalDateTime replyCreated;
    private Long preReplyId;

    private String username;
    private String userImgPath;
}
