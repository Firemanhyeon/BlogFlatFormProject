package org.blog.blogflatformproject.board.repository;

import org.blog.blogflatformproject.blog.domain.Blog;
import org.blog.blogflatformproject.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board,Long> {

    List<Board> findByBlogAndOpenYnTrueAndTemporaryYnTrue(Blog blog);

    @Query("SELECT b FROM Board b where b.openYn=true and b.temporaryYn=true ORDER BY b.createAt DESC")
    List<Board> findAllOrderByCreateAtDesc();

    @Modifying
    @Query("UPDATE Board b SET b.visitCount = b.visitCount + 1 WHERE b.boardId = :boardId")
    void updateVisitCnt(@Param("boardId")Long boardId);
    }
