package org.blog.blogflatformproject.board.event;

import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.board.service.ViewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomEventListener {
    private static final Logger logger = LoggerFactory.getLogger(CustomEventListener.class);
    private final ViewService viewService;

    @EventListener
    public void handleCustomEvent(CustomEvent customEvent){
        viewService.saveView(customEvent.getBoardId(), customEvent.getUserId());
    }
}
