package org.blog.blogflatformproject.board.service;

import org.blog.blogflatformproject.board.domain.Tag;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface TagService {
    Set<Tag> addOrFind(String tags);

}
