package org.blog.blogflatformproject.board.domain;

import lombok.Getter;
import lombok.Setter;
import org.blog.blogflatformproject.blog.domain.Blog;

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
