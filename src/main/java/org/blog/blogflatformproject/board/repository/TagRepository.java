package org.blog.blogflatformproject.board.repository;

import org.blog.blogflatformproject.board.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag,Long> {
    public Tag findByTagName(String tagName);

}
