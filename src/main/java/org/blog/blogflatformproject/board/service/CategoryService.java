package org.blog.blogflatformproject.board.service;

import org.blog.blogflatformproject.board.domain.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {

    List<Category> getPreCategory();
    List<Category> getDownCategory(Long preCategoryId);
}
