package org.blog.blogflatformproject.board.event;

import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.board.service.ViewService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomEventListner {

    private final ViewService viewService;

    @EventListener
    public void handleCustomEvent(CustomEvent customEvent){
        viewService.saveView(customEvent.getBoardId(),customEvent.getUserId());
    }
}
