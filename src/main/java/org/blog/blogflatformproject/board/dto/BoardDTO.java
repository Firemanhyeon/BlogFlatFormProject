package org.blog.blogflatformproject.board.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BoardDTO {
    private Long boardId;
    private String boardTitle;
    private String boardContent;
    private String blogName;
    private Long blogId;
    private LocalDate createAt;
}
