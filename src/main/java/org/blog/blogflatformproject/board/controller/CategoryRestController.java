package org.blog.blogflatformproject.board.controller;

import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.board.domain.Category;
import org.blog.blogflatformproject.board.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryRestController {

    private final CategoryService categoryService;

    @GetMapping("/getPreCategory")
    public List<Category> getPreCategory(){
        return categoryService.getPreCategory();
    }

    @GetMapping("/getDownCategory")
    public List<Category> getDownCategory(@RequestParam("preCategory") Long preCategoryId){
        return categoryService.getDownCategory(preCategoryId);
    }


}
