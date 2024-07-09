package org.blog.blogflatformproject.board.dto;

import lombok.Getter;
import lombok.Setter;
import org.blog.blogflatformproject.board.domain.Reply;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class BoardDTO {
    private Long boardId;
    private String boardTitle;
    private String boardContent;
    private String blogName;
    private Long blogId;
    private LocalDate createAt;

    private String userImgPath;
    private String userName;
    private Set<Reply> replies;
}
