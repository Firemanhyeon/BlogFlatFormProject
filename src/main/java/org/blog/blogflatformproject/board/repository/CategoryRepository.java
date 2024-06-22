package org.blog.blogflatformproject.board.repository;

import org.blog.blogflatformproject.board.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    public List<Category> findByPreCategoryIsNull();
    public List<Category> findByPreCategory(Long id);
}
