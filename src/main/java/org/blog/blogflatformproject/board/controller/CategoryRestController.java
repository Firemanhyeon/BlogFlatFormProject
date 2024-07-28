package org.blog.blogflatformproject.board.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.board.domain.Category;
import org.blog.blogflatformproject.board.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryRestController {

    private final CategoryService categoryService;

    @GetMapping("/getPreCategory")
    public List<Category> getPreCategory() {
        return categoryService.getPreCategory();
    }

    @GetMapping("/getDownCategory")
    public List<Category> getDownCategory(@RequestParam("preCategory") Long preCategoryId) {
        return categoryService.getDownCategory(preCategoryId);
    }


}
