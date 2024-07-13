package org.blog.blogflatformproject.board.service.impl;

import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.board.domain.Category;
import org.blog.blogflatformproject.board.repository.CategoryRepository;
import org.blog.blogflatformproject.board.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    //상위카테고리 전부 가져오기
    @Override
    public List<Category> getPreCategory(){
        return categoryRepository.findByPreCategoryIsNull();
    }
    //선택한 상위카테고리에 대한 하위카테고리 전부 가져오기
    @Override
    public List<Category> getDownCategory(Long preCategoryId){
        return categoryRepository.findByPreCategory(preCategoryId);
    }
}
