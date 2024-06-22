package org.blog.blogflatformproject.board.domain;

import lombok.Getter;
import lombok.Setter;
import org.blog.blogflatformproject.blog.domain.Blog;

import java.time.LocalDate;

@Getter
@Setter
public class BoardDTO {
    private String boardTitle;
    private String boardContent;
    private String blogName;
    private LocalDate createAt;
}
