package org.blog.blogflatformproject.board.dto;

import lombok.Getter;
import lombok.Setter;
import org.blog.blogflatformproject.board.domain.Reply;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class BoardDTO {
    private Long boardId;
    private String boardTitle;
    private String boardContent;
    private String blogName;
    private Long blogId;
    private LocalDateTime createAt;
    private String firstImagePath;
    private int visitCount;

    private String userImgPath;
    private String userName;
    private Set<Reply> replies;
}
