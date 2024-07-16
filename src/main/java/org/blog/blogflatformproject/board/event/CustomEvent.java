package org.blog.blogflatformproject.board.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CustomEvent extends ApplicationEvent {
    private Long boardId;
    private Long userId;
    public CustomEvent(Object source,Long boardId,Long userId) {
        super(source);
        this.boardId=boardId;
        this.userId=userId;
    }
}
