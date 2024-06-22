package org.blog.blogflatformproject.board.service;

import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.board.domain.Category;
import org.blog.blogflatformproject.board.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    //상위카테고리 전부 가져오기
    public List<Category> getPreCategory(){
        return categoryRepository.findByPreCategoryIsNull();
    }
    //선택한 상위카테고리에 대한 하위카테고리 전부 가져오기
    public List<Category> getDownCategory(Long preCategoryId){
        return categoryRepository.findByPreCategory(preCategoryId);
    }
}
